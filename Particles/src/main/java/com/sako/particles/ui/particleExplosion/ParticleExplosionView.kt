package com.sako.particles.ui.particleExplosion

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sako.particles.R
import com.sako.particles.model.ExplosionParticle
import com.sako.particles.utils.TimerIntegration
import com.sako.particles.utils.Tools.generateRandomColor
import kotlin.math.min
import kotlin.random.Random

class ParticleExplosionView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val animator = ValueAnimator.ofFloat(0f, 1f)
    private val particleList = mutableListOf<ExplosionParticle>()
    private var maxVelocity: Float = 0f
    private var timer: TimerIntegration = TimerIntegration()


    var particlesCount = 150 //total number of particles
    var particlesColor = Color.RED
    var randomColor = false // flag to color each particle randomly
    var maxSize: Float = 8f //max size of the particles
    var minSize: Float = 2f //min size of the particles
    var trailLength: Int = 10

    init {
        setViewAttributes()
    }

    /**
     * Get attributes passed from xml
     * */
    private fun setViewAttributes() {
        val arr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ParticleExplosionView)

        particlesCount = arr.getInt(R.styleable.ParticleExplosionView_partExp_particlesCount, 150)
        particlesColor =
            arr.getColor(R.styleable.ParticleExplosionView_partExp_particlesColor, Color.RED)
        randomColor = arr.getBoolean(R.styleable.ParticleExplosionView_partExp_randomColor, false)
        maxSize = arr.getFloat(R.styleable.ParticleExplosionView_partExp_maxSize, 8f)
        minSize = arr.getFloat(R.styleable.ParticleExplosionView_partExp_minSize, 2f)
        trailLength = arr.getInt(R.styleable.ParticleExplosionView_partExp_trailLength, 10)

        arr.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //setup max velocity based on view size
        maxVelocity = (width * height) / ((width + height) * 45).toFloat()
    }

    /**
     * prepare and start explosion animation
     * */
    fun startExplosion() {

        //clear list
        particleList.clear()

        //create the particles and add them to the list
        repeat(particlesCount) {
            particleList.add(
                ExplosionParticle(
                    x = width / 2f,
                    y = height / 2f,
                    xVelocity = Random.nextDouble(
                        maxVelocity * -1.0,
                        maxVelocity.toDouble()
                    ).toFloat(),
                    yVelocity = Random.nextDouble(
                        maxVelocity * -1.0,
                        maxVelocity.toDouble()
                    ).toFloat(),
                    maxVelocity = maxVelocity,
                    acceleration = Random.nextDouble(1.15, 1.2).toFloat(),
                    size = Random.nextDouble(minSize.toDouble(), maxSize.toDouble()).toFloat(),
                    history = mutableListOf(),
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

        invalidate()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val deltaTime = timer.getDeltaTime()

        particleList.removeIf { it.paint.alpha <= 0 }

        particleList.forEach { particle ->

            //draw the trail
            particle.history.forEachIndexed { index, vector ->
                canvas.drawCircle(
                    vector.x,
                    vector.y,
                    min(particle.size, (index).toFloat()),
                    particle.paint
                )
            }

            //draw the main particle
            canvas.drawCircle(particle.x, particle.y, particle.size, particle.paint)

            particle.move(width, height, trailLength,deltaTime)
        }


        //if there are no more particles, do not invalidate view
        if (particleList.isEmpty()) {
            timer.reset()
        } else {
            postInvalidateOnAnimation()
        }

    }


    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        timer.reset()
    }

}