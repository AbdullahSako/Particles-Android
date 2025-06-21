package com.sako.particles.ui.spaceTravel

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.sako.particles.R
import com.sako.particles.model.SpaceTravelStarParticle
import com.sako.particles.utils.TimerIntegration

class SpaceTravelView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var starParticleCount = 150
    var starTrailCount = 4

    private val starParticleList = mutableListOf<SpaceTravelStarParticle>()
    private val timer = TimerIntegration()

    var isRunning = false


    init {
        setViewAttributes()
    }

    /**
     * Get attributes passed from xml
     * */
    private fun setViewAttributes() {
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SpaceTravelView)

        starParticleCount = arr.getInt(R.styleable.SpaceTravelView_spaceTravelParticleCount, 150)
        starTrailCount = arr.getInt(R.styleable.SpaceTravelView_spaceTravelParticleTrailCount, 4)

        arr.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        //clear in order to avoid duplication
        starParticleList.clear()

        //prepare the star particles
        repeat(starParticleCount) {
            starParticleList.add(
                SpaceTravelStarParticle.newStarInstance(w, h)
            )
        }

        //draw the initial still particles
        invalidate()
    }


    /**
     * Starts the animation of the particles,
     * This is done by running an infinite ValueAnimator and calling postInvalidateOnAnimation() on each frame
     * */
    fun startAnimation() {
        isRunning = true
        invalidate()
    }

    /**
     * stops the animation of the particles
     * */
    fun stopAnimation() {
        starParticleList.forEach { it.stop() }
        isRunning = false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val deltaTime = timer.getDeltaTime()


        starParticleList.forEach {


            //draw the particle trail
            for (i in 0 until it.history.count()) {
                val radius = if (it.size > i) i.toFloat() else it.size
                val vector = it.history[i]

                canvas.drawCircle(
                    vector.x,
                    vector.y,
                    radius,
                    it.paint
                )
            }


            //draw the particle
            canvas.drawCircle(it.x, it.y, it.size, it.paint)

            //move the particle
            it.move(width, height, trailCount = starTrailCount, deltaTime)

        }

        if (isRunning) {
            postInvalidateOnAnimation()
        } else {
            timer.reset()
        }
    }


    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        timer.reset()
    }

}