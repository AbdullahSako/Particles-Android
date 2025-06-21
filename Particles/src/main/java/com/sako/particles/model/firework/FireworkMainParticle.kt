package com.sako.particles.model.firework

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.sako.particles.model.Vector
import com.sako.particles.utils.Tools.generateRandomColor
import kotlin.math.sqrt
import kotlin.random.Random

data class FireworkMainParticle(
    var x: Float,
    var y: Float,
    var yVelocity: Float,
    var explosionYCoordinate: Float,
    val history: MutableList<Vector>,
    val explosionParticleList: MutableList<FireworkExplosionParticle>,
    var explosionFadeRectangle: Rect,
    val size: Float,
    val paint: Paint,
    var isExploded: Boolean = false
) {


    fun move(trailMaxCount: Int) {


        //add the current position to the history
        history.add(Vector(x, y))


        //move particle
        y += yVelocity


        //limit the trail list to trailMaxCount
        if (history.count() > trailMaxCount) {
            history.removeAt(0)
        }


        //check if particle has reached the explosion point
        if (y < explosionYCoordinate) {
            isExploded = true
        }

    }


    /**
     * Reset particle parameters and position to a new random position
     * */
    fun resetParticle(
        w: Int, h: Int, mainParticleCount: Int,
         explosionAreaMaxSize: Int = -1
    ) {

        //calculate explosion area size if not specified
        var explosionAreaSize =
            if (explosionAreaMaxSize == -1) (sqrt((w * h) / 2 / mainParticleCount.toDouble()).toInt()) else explosionAreaMaxSize


        //add slight variation for each particle explosionAreaSize, as it effects velocity, this should make the explosion shape unique
        explosionAreaSize =
            Random.nextInt(explosionAreaSize - 100, explosionAreaSize + 100).coerceAtMost(400)

        //calculate explosion max velocity based on explosion area size
        val explosionMaxVelocity =
            (explosionAreaSize * explosionAreaSize) / ((explosionAreaSize + explosionAreaSize) * 75).toFloat()

        //generate random x coordinate
        val x = Random.nextDouble(0.0, w.toDouble()).toFloat()

        //main particle explosion y coordinate
        val explosionYCoordinate = Random.nextDouble(h * 0.05, h * 0.5).toFloat()


        //This rectangle is used to fade the explosion particles when they leave the rectangle coordinates
        val explosionFadeLimitRect = Rect(
            x.toInt() - explosionAreaSize,
            explosionYCoordinate.toInt() - explosionAreaSize,
            x.toInt() + explosionAreaSize,
            explosionYCoordinate.toInt() + explosionAreaSize
        )

        this.x = x
        this.y = Random.nextDouble(h.toDouble(), h * 1.5).toFloat()
        this.explosionYCoordinate = explosionYCoordinate
        this.history.clear()
        this.explosionFadeRectangle = explosionFadeLimitRect
        this.isExploded = false

        this.explosionParticleList.forEach {
            it.x = x
            it.y = explosionYCoordinate
            it.xVelocity = Random.nextDouble(
                explosionMaxVelocity * -1.0,
                explosionMaxVelocity.toDouble()
            ).toFloat()
            it.yVelocity = Random.nextDouble(
                explosionMaxVelocity * -1.0,
                explosionMaxVelocity.toDouble()
            ).toFloat()
            it.maxVelocity = explosionMaxVelocity
            it.acceleration = Random.nextDouble(1.15, 1.2).toFloat()
            it.history.clear()
            it.paint.alpha = 255
            it.lifeSpan = 100
        }



    }


    companion object {
        fun newFireworkInstance(
            w: Int,
            h: Int,
            mainParticleCount: Int,
            mainParticleColor: Int = Color.WHITE,
            mainParticleMinVelocity: Float = 10f,
            mainParticleMaxVelocity: Float = 20f,
            mainParticleSize: Float = 5f,
            explosionParticlesCount: Int = 25,
            explosionParticlesColor: Int = Color.RED,
            explosionRandomColor: Boolean = false,
            explosionParticleRandomColor: Boolean = false,
            explosionMaxSize: Float = 8f,
            explosionMinSize: Float = 2f,
            explosionAreaMaxSize: Int = -1
        ): FireworkMainParticle {

            //calculate explosion area size if not specified
            var explosionAreaSize =
                if (explosionAreaMaxSize == -1) (sqrt((w * h) / 2 / mainParticleCount.toDouble()).toInt()) else explosionAreaMaxSize


            //add slight variation for each particle explosionAreaSize, as it effects velocity, this should make the explosion shape unique
            explosionAreaSize =
                Random.nextInt(explosionAreaSize - 100, explosionAreaSize + 100).coerceAtMost(400)

            //calculate explosion max velocity based on explosion area size
            val explosionMaxVelocity =
                (explosionAreaSize * explosionAreaSize) / ((explosionAreaSize + explosionAreaSize) * 75).toFloat()

            //generate random x coordinate
            val x = Random.nextDouble(0.0, w.toDouble()).toFloat()

            //main particle explosion y coordinate
            val explosionYCoordinate = Random.nextDouble(h * 0.05, h * 0.5).toFloat()


            //This rectangle is used to fade the explosion particles when they leave the rectangle coordinates
            val explosionFadeLimitRect = Rect(
                x.toInt() - explosionAreaSize,
                explosionYCoordinate.toInt() - explosionAreaSize,
                x.toInt() + explosionAreaSize,
                explosionYCoordinate.toInt() + explosionAreaSize
            )

            //generate random color for each firework if enabled
            val mExplosionParticlesColor = if (explosionRandomColor) {
                generateRandomColor()
            } else {
                explosionParticlesColor
            }

            //create a list of explosion particles for each main particle
            val explosionParticleList = mutableListOf<FireworkExplosionParticle>()
            repeat(explosionParticlesCount) {
                explosionParticleList.add(
                    FireworkExplosionParticle(
                        x = x,
                        y = explosionYCoordinate,
                        xVelocity = Random.nextDouble(
                            explosionMaxVelocity * -1.0,
                            explosionMaxVelocity.toDouble()
                        ).toFloat(),
                        yVelocity = Random.nextDouble(
                            explosionMaxVelocity * -1.0,
                            explosionMaxVelocity.toDouble()
                        ).toFloat(),
                        maxVelocity = explosionMaxVelocity,
                        acceleration = Random.nextDouble(1.15, 1.2).toFloat(),
                        size = Random.nextDouble(
                            explosionMinSize.toDouble(),
                            explosionMaxSize.toDouble()
                        ).toFloat(),
                        history = mutableListOf(),
                        paint = Paint().apply {
                            //if randomColor is enabled, generate a random color for each particle, else set the specified color
                            color = if (explosionParticleRandomColor) {
                                generateRandomColor()
                            } else {
                                mExplosionParticlesColor
                            }

                        },
                        lifeSpan = 100,
                        maxLifeSpan = 100
                    )
                )
            }


            //create and return the main particle
            return FireworkMainParticle(
                x = x,
                y = Random.nextDouble(h.toDouble(), h * 1.5).toFloat(),
                yVelocity = Random.nextDouble(
                    -mainParticleMaxVelocity.toDouble(),
                    -mainParticleMinVelocity.toDouble()
                ).toFloat(),
                explosionYCoordinate = explosionYCoordinate,
                history = mutableListOf(),
                explosionParticleList = explosionParticleList,
                explosionFadeRectangle = explosionFadeLimitRect,
                size = mainParticleSize,
                paint = Paint().apply {
                    color = mainParticleColor
                },
                isExploded = false

            )
        }
    }


}