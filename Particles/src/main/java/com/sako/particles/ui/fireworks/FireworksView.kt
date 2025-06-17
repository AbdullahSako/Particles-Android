package com.sako.particles.ui.fireworks

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.sako.particles.R
import com.sako.particles.model.Vector
import com.sako.particles.model.firework.FireworkExplosionParticle
import com.sako.particles.model.firework.FireworkMainParticle
import com.sako.particles.utils.Tools.logd

class FireworksView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    var maxFireworkCount = 10
    var mainParticleColor: Int = Color.WHITE
    var mainParticleMinVelocity: Float = 5f
    var mainParticleMaxVelocity: Float = 10f
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
    private val animator = ValueAnimator.ofFloat(0f, 1f)

    init {
        setViewAttributes()
    }

    /**
     * Get attributes passed from xml
     * */
    private fun setViewAttributes() {
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FireworksView)

        maxFireworkCount = arr.getInt(R.styleable.FireworksView_fireworkCount, 10)
        mainParticleColor = arr.getColor(R.styleable.FireworksView_fireworkMainParticleColor, Color.WHITE)
        mainParticleMinVelocity = arr.getFloat(R.styleable.FireworksView_fireworkMainParticleMinVelocity, 5f)
        mainParticleMaxVelocity = arr.getFloat(R.styleable.FireworksView_fireworkMainParticleMaxVelocity, 10f)
        mainParticleSize = arr.getFloat(R.styleable.FireworksView_fireworkMainParticleSize, 3f)
        mainParticleTrailCount = arr.getInt(R.styleable.FireworksView_fireworkMainParticleTrailCount, 5)

        explosionParticlesCount = arr.getInt(R.styleable.FireworksView_fireworkExplosionParticleCount, 20)
        explosionParticlesColor = arr.getColor(R.styleable.FireworksView_fireworkExplosionParticleColor, Color.RED)
        explosionRandomColor = arr.getBoolean(R.styleable.FireworksView_fireworkExplosionRandomColor, true)
        explosionParticleRandomColor = arr.getBoolean(R.styleable.FireworksView_fireworkExplosionParticleRandomColor, false)
        explosionMaxSize = arr.getFloat(R.styleable.FireworksView_fireworkExplosionParticleMaxSize, 8f)
        explosionMinSize = arr.getFloat(R.styleable.FireworksView_fireworkExplosionParticleMinSize, 2f)
        explosionAreaMaxSize = arr.getInt(R.styleable.FireworksView_fireworkExplosionAreaMaxSize, -1)
        explosionTrailCount = arr.getInt(R.styleable.FireworksView_fireworkExplosionParticleTrailCount, 6)
        explosionSmudge = arr.getBoolean(R.styleable.FireworksView_fireworkExplosionSmudge, false)

        arr.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mainFireworkParticleList.clear()

        //prepare the firework particles
        repeat(maxFireworkCount) {
            mainFireworkParticleList.add(
                createMainFireworkParticleInstance(w, h)
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
            postInvalidateDelayed(16)

            //add new firework particles as the old ones are exploded and removed
            while (mainFireworkParticleList.count { !it.isExploded } < maxFireworkCount) {
                mainFireworkParticleList.add(
                    createMainFireworkParticleInstance(width, height)
                )
            }
        }

        animator.start()
    }

    override fun onDraw(canvas: Canvas) {

        val mainIterator = mainFireworkParticleList.iterator()
        while (mainIterator.hasNext()) {
            val mainParticle = mainIterator.next()

            //remove main particles that have completely exploded and faded out
            if(mainParticle.explosionParticleList.isEmpty()){
                mainIterator.remove()
                continue
            }

            //draw main particle if it has not exploded yet
            if (!mainParticle.isExploded) {

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
                mainParticle.move(mainParticleTrailCount)

            } else {
                val explosionIterator = mainParticle.explosionParticleList.iterator()

                //iterate over explosion particles
                while (explosionIterator.hasNext()){
                    val explosionParticle = explosionIterator.next()

                    //remove explosion particles that have completely faded out
                    if(explosionParticle.paint.alpha <= 0f ){
                        explosionIterator.remove()
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
                        explosionSmudge
                    )


                }
            }
        }


    }

    private fun drawTrail(
        canvas: Canvas,
        history: MutableList<Vector>,
        size: Float,
        paint: Paint
    ) {

        for (i in 0 until history.count()){
            val radius = if (size > i) i.toFloat() else size
            val vector= history[i]


            canvas.drawCircle(
                vector.x,
                vector.y,
                radius,
                paint
            )
        }



    }

    private fun createMainFireworkParticleInstance(w: Int, h: Int): FireworkMainParticle {
        return FireworkMainParticle.newFireworkInstance(
            width,
            height,
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
    }

}