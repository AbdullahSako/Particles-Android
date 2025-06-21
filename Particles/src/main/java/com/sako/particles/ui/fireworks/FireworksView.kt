package com.sako.particles.ui.fireworks

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sako.particles.R
import com.sako.particles.model.Vector
import com.sako.particles.model.firework.FireworkMainParticle
import com.sako.particles.utils.TimerIntegration

class FireworksView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    var maxFireworkCount = 10
    var mainParticleColor: Int = Color.WHITE
    var mainParticleMinVelocity: Float = 15f
    var mainParticleMaxVelocity: Float = 25f
    var mainParticleSize: Float = 3f
    var mainParticleTrailCount = 5


    var explosionParticlesCount: Int = 20
    var explosionParticlesColor: Int = Color.RED
    var explosionRandomColor: Boolean = true
    var explosionParticleRandomColor: Boolean = false
    var explosionMaxSize: Float = 8f
    var explosionMinSize: Float = 2f
    var explosionAreaMaxSize: Int = -1
    var explosionTrailCount: Int = 6
    var explosionSmudge: Boolean = false

    private val mainFireworkParticleList = mutableListOf<FireworkMainParticle>()
    private var timer: TimerIntegration = TimerIntegration()


    init {
        setViewAttributes()
    }

    /**
     * Get attributes passed from xml
     * */
    private fun setViewAttributes() {
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FireworksView)

        maxFireworkCount = arr.getInt(R.styleable.FireworksView_fireworkCount, 10)
        mainParticleColor =
            arr.getColor(R.styleable.FireworksView_fireworkMainParticleColor, Color.WHITE)
        mainParticleMinVelocity =
            arr.getFloat(R.styleable.FireworksView_fireworkMainParticleMinVelocity, 15f)
        mainParticleMaxVelocity =
            arr.getFloat(R.styleable.FireworksView_fireworkMainParticleMaxVelocity, 25f)
        mainParticleSize = arr.getFloat(R.styleable.FireworksView_fireworkMainParticleSize, 3f)
        mainParticleTrailCount =
            arr.getInt(R.styleable.FireworksView_fireworkMainParticleTrailCount, 5)

        explosionParticlesCount =
            arr.getInt(R.styleable.FireworksView_fireworkExplosionParticleCount, 20)
        explosionParticlesColor =
            arr.getColor(R.styleable.FireworksView_fireworkExplosionParticleColor, Color.RED)
        explosionRandomColor =
            arr.getBoolean(R.styleable.FireworksView_fireworkExplosionRandomColor, true)
        explosionParticleRandomColor =
            arr.getBoolean(R.styleable.FireworksView_fireworkExplosionParticleRandomColor, false)
        explosionMaxSize =
            arr.getFloat(R.styleable.FireworksView_fireworkExplosionParticleMaxSize, 8f)
        explosionMinSize =
            arr.getFloat(R.styleable.FireworksView_fireworkExplosionParticleMinSize, 2f)
        explosionAreaMaxSize =
            arr.getInt(R.styleable.FireworksView_fireworkExplosionAreaMaxSize, -1)
        explosionTrailCount =
            arr.getInt(R.styleable.FireworksView_fireworkExplosionParticleTrailCount, 6)
        explosionSmudge = arr.getBoolean(R.styleable.FireworksView_fireworkExplosionSmudge, false)

        arr.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mainFireworkParticleList.clear()

        //prepare the firework particles
        repeat(maxFireworkCount) {
            mainFireworkParticleList.add(
                FireworkMainParticle.newFireworkInstance(
                    w,
                    h,
                    maxFireworkCount,
                    mainParticleColor,
                    mainParticleMinVelocity,
                    mainParticleMaxVelocity,
                    mainParticleSize,
                    explosionParticlesCount,
                    explosionParticlesColor,
                    explosionRandomColor,
                    explosionParticleRandomColor,
                    explosionMaxSize,
                    explosionMinSize,
                    explosionAreaMaxSize
                )
            )

        }

    }


    override fun onDraw(canvas: Canvas) {
        val deltaTime = timer.getDeltaTime()

        mainFireworkParticleList.forEach { mainParticle ->

            if (mainParticle.isExploded) {


                var particlesFadedOut = 0
                for (explosionParticle in mainParticle.explosionParticleList) {

                    //if explosion particle faded out, add it to the count
                    if (explosionParticle.paint.alpha <= 0f) {
                        particlesFadedOut++
                        continue
                    }


                    //draw the explosion particle trail
                    drawTrail(
                        canvas,
                        explosionParticle.history,
                        explosionParticle.size,
                        explosionParticle.paint
                    )

                    //draw explosion particle
                    canvas.drawCircle(
                        explosionParticle.x,
                        explosionParticle.y,
                        explosionParticle.size,
                        explosionParticle.paint
                    )


                    //move explosion particle
                    explosionParticle.move(
                        explosionTrailCount,
                        mainParticle.explosionFadeRectangle,
                        explosionSmudge,deltaTime
                    )


                }

                //if all explosion particles faded out, reset main particle
                if (particlesFadedOut == mainParticle.explosionParticleList.count()) {
                    mainParticle.resetParticle(
                        width,
                        height,
                        maxFireworkCount,
                        explosionAreaMaxSize
                    )
                }


            } else {

                //draw main particle trail
                drawTrail(canvas, mainParticle.history, mainParticle.size, mainParticle.paint)


                //draw main body
                canvas.drawCircle(
                    mainParticle.x,
                    mainParticle.y,
                    mainParticle.size,
                    mainParticle.paint
                )


                //move main particle
                mainParticle.move(mainParticleTrailCount,deltaTime)
            }

        }


        postInvalidateOnAnimation()

    }

    private fun drawTrail(
        canvas: Canvas,
        history: MutableList<Vector>,
        size: Float,
        paint: Paint
    ) {

        for (i in 0 until history.count()) {
            val radius = if (size > i) i.toFloat() else size
            val vector = history[i]

            canvas.drawCircle(
                vector.x,
                vector.y,
                radius,
                paint
            )
        }


    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        timer.reset()
    }

}