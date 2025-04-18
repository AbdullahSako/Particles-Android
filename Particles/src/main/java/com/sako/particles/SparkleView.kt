package com.sako.particles

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class SparkleView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    //-----attributes-----
    var particlesCount = 150 //total number of particles
    var particlesColor = Color.GREEN
    var randomColor = false // flag to color each particle randomly
    var enableTopFadingEdge = false //flag to enable fading edge at the top
    var enableBottomFadingEdge = false //flag to enable fading edge at the bottom
    var enableLeftFadingEdge = false //flag to enable fading edge at the left
    var enableRightFadingEdge = false //flag to enable fading edge at the right
    var enableTouchToAccelerate = true //flag to enable touch to accelerate the particles away from touch point

    var fadeWidth = 100f // Width of the fading area
    @ColorInt
    var fadeColor = Color.WHITE // Color of the fading area

    @FloatRange(0.0, 5.0, false, true)
    var maxVelocity: Float = 0.5f //max velocity of the particles
    var maxAcceleration: Float = 1.33f //max acceleration of the particles
    var maxSize: Float = 3f //max size of the particles


    private val particleList = mutableListOf<Particle>()
    private var leftFadePaint: Paint = Paint()
    private var rightFadePaint: Paint = Paint()
    private var bottomFadePaint: Paint = Paint()
    private var topFadePaint: Paint = Paint()


    init {
        setViewAttributes()
    }

    /**
     * Get attributes passed from xml
     * */
    private fun setViewAttributes() {
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SparkleView)

         particlesCount = arr.getInt(R.styleable.SparkleView_particlesCount,100)
         particlesColor =   arr.getColor(R.styleable.SparkleView_particlesColor, Color.GREEN)
         randomColor = arr.getBoolean(R.styleable.SparkleView_randomColor,false)
         enableTopFadingEdge = arr.getBoolean(R.styleable.SparkleView_enableTopFadingEdge,false)
         enableBottomFadingEdge =arr.getBoolean(R.styleable.SparkleView_enableBottomFadingEdge,false)
         enableLeftFadingEdge = arr.getBoolean(R.styleable.SparkleView_enableLeftFadingEdge,false)
         enableRightFadingEdge =arr.getBoolean(R.styleable.SparkleView_enableRightFadingEdge,false)
         enableTouchToAccelerate =arr.getBoolean(R.styleable.SparkleView_enableTouchToAccelerate,false)

         fadeWidth = arr.getFloat(R.styleable.SparkleView_fadeWidth,100f)
         fadeColor = arr.getColor(R.styleable.SparkleView_fadeColor,Color.WHITE)

         maxVelocity = arr.getFloat(R.styleable.SparkleView_maxVelocity,0.5f)
         maxAcceleration = arr.getFloat(R.styleable.SparkleView_maxAcceleration,1.33f)
         maxSize = arr.getFloat(R.styleable.SparkleView_maxSize,3f)

        arr.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        setupFadingEdgePaint()

        //create the particles and add them to the list
        repeat(particlesCount) {
            particleList.add(
                Particle(
                    x = Random.nextDouble(0.0, w.toDouble()).toFloat(),
                    y = Random.nextDouble(0.0, h.toDouble()).toFloat(),
                    xVelocity = Random.nextDouble(
                        maxVelocity.toDouble() * -1,
                        maxVelocity.toDouble()
                    ).toFloat(),
                    yVelocity = Random.nextDouble(
                        maxVelocity.toDouble() * -1,
                        maxVelocity.toDouble()
                    ).toFloat(),
                    maxVelocity = maxVelocity,
                    acceleration = 1.0f,
                    maxAccel = maxAcceleration,
                    size = Random.nextDouble(1.0, maxSize.toDouble()).toFloat(),
                    alpha = Random.nextFloat(),
                    alphaChangeValue = Random.nextDouble(0.0, 0.03).toFloat(),
                    paint = Paint().apply {
                        //if randomColor is enabled, generate a random color for each particle, else set the specified color
                        color = if (randomColor) {
                            generateRandomColor()
                        } else {
                            particlesColor
                        }
                    })
            )
        }

        startAnimation()
    }


    /**
     * Starts the animation of the particles,
     * This is done by running an infinite ValueAnimator and calling postInvalidateOnAnimation() on each frame
     * */
    private fun startAnimation() {

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.RESTART
        animator.addUpdateListener {
            postInvalidateOnAnimation()
        }

        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //draw particles
        repeat(particlesCount) {
            canvas.drawCircle(
                particleList[it].x,
                particleList[it].y,
                particleList[it].size,
                particleList[it].paint
            )

            //move the particle
            particleList[it].move(width, height)
        }


        //-------draw fading edges------
        if (enableLeftFadingEdge) {
            canvas.drawPaint(leftFadePaint)
        }
        if (enableRightFadingEdge) {
            canvas.drawPaint(rightFadePaint)
        }
        if (enableTopFadingEdge) {
            canvas.drawPaint(topFadePaint)
        }
        if (enableBottomFadingEdge) {
            canvas.drawPaint(bottomFadePaint)
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        //if enableTouchToAccelerate is enabled, accelerate the particles away from the touch point
        if (enableTouchToAccelerate) {
            if (event?.action == MotionEvent.ACTION_DOWN) {
                for (particle in particleList) {

                    //calculate the distance between the current particle and the touch point
                    val distanceBetweenTwoPoints =
                        sqrt((particle.x - event.x).pow(2) + (particle.y - event.y).pow(2))

                    //if the distance is more than 300, accelerate the particle away from the touch point
                    if (distanceBetweenTwoPoints < 300f) {
                        //accelerate the particle away from the touch point
                        particle.accelerateAwayFrom(event.x, event.y, distanceBetweenTwoPoints)
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }

    /**
     * Generates a random color
     * */
    private fun generateRandomColor(): Int {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return Color.rgb(red, green, blue)
    }

    /**
     * sets the alpha of a color
     * */
    private fun Int.setColorAlpha(@IntRange(0, 255) alpha: Int): Int {
        return Color.argb(alpha, this.red, this.green, this.blue)
    }

    /**
     * sets up a linear gradient paint for each edge
     * */
    private fun setupFadingEdgePaint() {

        //The positioning of the gradient colors
        val gradientColorPositioning = floatArrayOf(
            0f,
            (1 - (fadeWidth / width)) - (fadeWidth / width),
            1 - (fadeWidth / width),
            1f
        )

        //left side
        leftFadePaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, fadeWidth, 0f,
                intArrayOf(
                    fadeColor,
                    fadeColor.setColorAlpha(50),
                    fadeColor.setColorAlpha(10),
                    fadeColor.setColorAlpha(0)
                ),
                gradientColorPositioning,
                Shader.TileMode.CLAMP
            )
        }

        //right side
        rightFadePaint = Paint().apply {
            shader = LinearGradient(
                width.toFloat(), 0f, width - fadeWidth, 0f,
                intArrayOf(
                    fadeColor,
                    fadeColor.setColorAlpha(50),
                    fadeColor.setColorAlpha(10),
                    fadeColor.setColorAlpha(0)
                ),
                gradientColorPositioning,
                Shader.TileMode.CLAMP
            )
        }

        //bottom side
        bottomFadePaint = Paint().apply {
            shader = LinearGradient(
                0f, height.toFloat(), 0f, height - fadeWidth,
                intArrayOf(
                    fadeColor,
                    fadeColor.setColorAlpha(50),
                    fadeColor.setColorAlpha(10),
                    fadeColor.setColorAlpha(0)
                ),
                gradientColorPositioning,
                Shader.TileMode.CLAMP
            )
        }

        //top side
        topFadePaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, fadeWidth,
                intArrayOf(
                    fadeColor,
                    fadeColor.setColorAlpha(50),
                    fadeColor.setColorAlpha(10),
                    fadeColor.setColorAlpha(0)
                ),
                gradientColorPositioning,
                Shader.TileMode.CLAMP
            )
        }

    }

}