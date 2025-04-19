package com.sako.particles.ui.sparkles

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.sako.particles.R
import com.sako.particles.model.SparklesParticle
import com.sako.particles.utils.Tools.distanceBetweenTwoPoints
import com.sako.particles.utils.Tools.generateRandomColor
import com.sako.particles.utils.Tools.logd
import com.sako.particles.utils.Tools.setColorAlpha
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class SparklesView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    //-----attributes-----
    var particlesCount = 150 //total number of particles
    var particlesColor = Color.GREEN
    var randomColor = false // flag to color each particle randomly
    var connectLineColor = Color.RED //the color of the lines that connect the particles
    var enableTopFadingEdge = false //flag to enable fading edge at the top
    var enableBottomFadingEdge = false //flag to enable fading edge at the bottom
    var enableLeftFadingEdge = false //flag to enable fading edge at the left
    var enableRightFadingEdge = false //flag to enable fading edge at the right
    var enableTouchToAccelerate =
        true //flag to enable touch to accelerate the particles away from touch point
    var enableParticleConnect = false
    var maxParticleConnectDistance = 100f

    var fadeWidth = 100f // Width of the fading area

    @ColorInt
    var fadeColor = Color.WHITE // Color of the fading area

    @FloatRange(0.0, 5.0, false, true)
    var maxVelocity: Float = 0.5f //max velocity of the particles
    var maxAcceleration: Float = 1.33f //max acceleration of the particles
    var maxSize: Float = 3f //max size of the particles

    var sparkleViewBackgroundColor: Int = Color.TRANSPARENT
    var radius = 0f


    private val particleList = mutableListOf<SparklesParticle>()
    private var leftFadePaint: Paint = Paint()
    private var rightFadePaint: Paint = Paint()
    private var bottomFadePaint: Paint = Paint()
    private var topFadePaint: Paint = Paint()
    private var bgPaint: Paint = Paint()
    private var connectLinePaint: Paint = Paint()
    private val animator = ValueAnimator.ofFloat(0f, 1f)
    private var cornerRadiusClipPath = Path()

    init {
        setViewAttributes()
    }

    /**
     * Get attributes passed from xml
     * */
    private fun setViewAttributes() {
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SparklesView)

        particlesCount = arr.getInt(R.styleable.SparklesView_particlesCount, 100)
        particlesColor = arr.getColor(R.styleable.SparklesView_particlesColor, Color.GREEN)
        randomColor = arr.getBoolean(R.styleable.SparklesView_randomColor, false)
        connectLineColor = arr.getColor(R.styleable.SparklesView_connectLineColor, Color.RED)
        enableTopFadingEdge = arr.getBoolean(R.styleable.SparklesView_enableTopFadingEdge, false)
        enableBottomFadingEdge =
            arr.getBoolean(R.styleable.SparklesView_enableBottomFadingEdge, false)
        enableLeftFadingEdge = arr.getBoolean(R.styleable.SparklesView_enableLeftFadingEdge, false)
        enableRightFadingEdge = arr.getBoolean(R.styleable.SparklesView_enableRightFadingEdge, false)
        enableTouchToAccelerate =
            arr.getBoolean(R.styleable.SparklesView_enableTouchToAccelerate, false)
        enableParticleConnect =
            arr.getBoolean(R.styleable.SparklesView_enableParticleConnect, false)
        maxParticleConnectDistance =
            arr.getFloat(R.styleable.SparklesView_maxParticleConnectDistance, 100f)

        fadeWidth = arr.getFloat(R.styleable.SparklesView_fadeWidth, 100f)
        fadeColor = arr.getColor(R.styleable.SparklesView_fadeColor, Color.WHITE)

        maxVelocity = arr.getFloat(R.styleable.SparklesView_maxVelocity, 0.5f)
        maxAcceleration = arr.getFloat(R.styleable.SparklesView_maxAcceleration, 1.33f)
        maxSize = arr.getFloat(R.styleable.SparklesView_maxSize, 3f)

        sparkleViewBackgroundColor =
            arr.getColor(R.styleable.SparklesView_sparkleViewBackgroundColor, Color.TRANSPARENT)
        radius = arr.getFloat(R.styleable.SparklesView_sparkleViewRadius, 0f)

        arr.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        setupFadingEdgePaint()

        //bg color paint
        bgPaint = Paint().apply {
            color = sparkleViewBackgroundColor
        }

        //connect line paint
        connectLinePaint = Paint().apply {
            color = connectLineColor
        }

        //clear list so that the particles are not duplicated as android recalls this method
        particleList.clear()

        //create the particles and add them to the list
        repeat(particlesCount) {
            particleList.add(
                SparklesParticle(
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

        //path for applying rounded corners
        cornerRadiusClipPath = Path().apply {
            addRoundRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                radius,
                radius,
                Path.Direction.CW
            )
        }

        startAnimation()
    }


    /**
     * Starts the animation of the particles,
     * This is done by running an infinite ValueAnimator and calling postInvalidateOnAnimation() on each frame
     * */
    private fun startAnimation() {


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

        //if radius is set, apply rounded corners
        if (radius != 0f) {
            canvas.clipPath(cornerRadiusClipPath)
        }

        //draw bg color
        canvas.drawPaint(bgPaint)

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

            //------Draw connect lines-------
            if(enableParticleConnect) { //check if flag is enables
                //iterate through all particles to check if the current particle drawn is within distance with any other particle
                for (particleTo in particleList) {

                    //check if the current particle is not the same as the particle being iterated through
                    if (particleTo != particleList[it]) {

                        //calculate the distance between the current particle and the particle being iterated through
                        val distanceBetweenParticles = distanceBetweenTwoPoints(
                            particleList[it].x,
                            particleList[it].y,
                            particleTo.x,
                            particleTo.y
                        )

                        //if within distance, draw a line between the two particles
                        if (distanceBetweenParticles < maxParticleConnectDistance) {
                            canvas.drawLine(
                                particleList[it].x,
                                particleList[it].y,
                                particleTo.x,
                                particleTo.y,
                                connectLinePaint
                            )
                        }
                    }
                }
            }
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


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }
}