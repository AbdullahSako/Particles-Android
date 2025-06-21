package com.sako.particles.model

import android.graphics.Paint
import com.sako.particles.utils.Constants.FRAME_RATE
import com.sako.particles.utils.Tools
import kotlin.random.Random

data class SnowParticle(
    var x: Float,
    var y: Float,
    var xVelocity: Float,
    var yVelocity: Float,
    var xAcceleration: Float,
    val size: Float,
    val paint: Paint,
    var isOutsideFrame: Boolean = false
) {


    /**
     * Move the particle based on its velocity and acceleration
     *
     *  @param viewWidth Canvas view width
     *  @param viewHeight Canvas view height
     * */
    fun move(viewWidth: Int, viewHeight: Int,deltaTime: Float) {

        //apply acceleration to the velocity
        xVelocity += xAcceleration

        //swing the snow particle
        if (xVelocity > 1f || xVelocity < -1f) {
            xAcceleration *= -1
        }

        //move the particle
        x += xVelocity * FRAME_RATE * deltaTime
        y += yVelocity * FRAME_RATE * deltaTime


        //if the particle falls under the view, set the isOutsideFrame flag to true
        if (y > viewHeight) {
            isOutsideFrame = true
        }
    }


    companion object {
        fun createSnowParticle(
            w: Int,
            h: Int,
            minSize: Float,
            maxSize: Float,
            particleColor: Int,
            randomColor: Boolean,
            randomStartPositions: Boolean
        ): SnowParticle {

            val y = if(randomStartPositions){
                //place snow particle randomly within the screen
                Random.nextDouble(-(h*0.4),h.toDouble()).toFloat()
            }else{
                //place snow particle at the top of the screen
                Random.nextDouble(-h.toDouble(), -1.0).toFloat()
            }


            return SnowParticle(
                x = Random.nextDouble(0.0, w.toDouble()).toFloat(),
                y = y,
                xVelocity = Random.nextDouble(-2.0, 2.0).toFloat(),
                yVelocity = Random.nextDouble(1.0, 1.7).toFloat(),
                xAcceleration = Random.nextDouble(0.001, 0.009).toFloat(),
                size = Random.nextDouble(minSize.toDouble(), maxSize.toDouble()).toFloat(),
                paint = Paint().apply {
                    if (randomColor) {
                        //random snow particle colors
                        color = Tools.generateRandomColor()
                    } else {
                        color = particleColor
                    }
                }

            )
        }
    }

}