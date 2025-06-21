package com.sako.particles.ui.snowfall

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.sako.particles.R
import com.sako.particles.model.SnowParticle
import com.sako.particles.utils.TimerIntegration

class SnowfallView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var snowParticleCount = 400
    var minParticleSize = 1f
    var maxParticleSize = 5f
    var particleColor = Color.WHITE
    var randomColor = false
    var randomStartPositions = true


    private val snowParticleList = mutableListOf<SnowParticle>()
    private val animator = ValueAnimator.ofFloat(0f, 1f)
    private var timer: TimerIntegration = TimerIntegration()


    init {
        setViewAttributes()
    }

    /**
     * Get attributes passed from xml
     * */
    private fun setViewAttributes() {
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SnowfallView)

        snowParticleCount = arr.getInt(R.styleable.SnowfallView_snowfallParticleCount, 400)
        minParticleSize = arr.getFloat(R.styleable.SnowfallView_snowfallMinParticleSize, 1f)
        maxParticleSize = arr.getFloat(R.styleable.SnowfallView_snowfallMaxParticleSize, 5f)
        particleColor = arr.getColor(R.styleable.SnowfallView_snowfallParticleColor, Color.WHITE)
        randomColor = arr.getBoolean(R.styleable.SnowfallView_snowfallRandomParticleColors, false)
        randomStartPositions = arr.getBoolean(R.styleable.SnowfallView_snowfallRandomParticleStartingPositions, true)

        arr.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        //clear list so that the particles are not duplicated as android recalls this method
        snowParticleList.clear()

        //create initial particles
        repeat(snowParticleCount) {
            snowParticleList.add(
                SnowParticle.createSnowParticle(
                    w,
                    h,
                    minParticleSize,
                    maxParticleSize,
                    particleColor,
                    randomColor,
                    randomStartPositions
                )
            )
        }



        invalidate()
    }




    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val deltaTime= timer.getDeltaTime()


        val snowParticleListIterator = snowParticleList.listIterator()
        while (snowParticleListIterator.hasNext()) {
            val snowParticle = snowParticleListIterator.next()

            //draw particle
            canvas.drawCircle(snowParticle.x, snowParticle.y, snowParticle.size, snowParticle.paint)

            //move particle
            snowParticle.move(width, height,deltaTime)


            //if snow particle is under the view
            if (snowParticle.isOutsideFrame) {

                //remove the particle from the list
                snowParticleListIterator.remove()

                //add a new snow particle to the list
                snowParticleListIterator.add(
                    SnowParticle.createSnowParticle(
                        width,
                        height,
                        minParticleSize,
                        maxParticleSize,
                        particleColor,
                        randomColor,
                        false
                    )
                )

            }

        }


        postInvalidateOnAnimation()
    }


    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        timer.reset()
    }


}