package com.sako.particles.model

import android.graphics.Color
import android.graphics.Paint
import com.sako.particles.utils.Constants.FRAME_RATE
import kotlin.random.Random

data class SpaceTravelStarParticle(
    var x: Float,
    var y: Float,
    var xVelocity: Float,
    var yVelocity: Float,
    var acceleration: Float,
    val history: MutableList<Vector> = mutableListOf(),
    var size: Float,
    var alpha: Float,
    val paint: Paint,
) {

    fun move(w: Int, h: Int, trailCount: Int, deltaTime: Float) {

        //add the current position to the history to draw a trail
        history.add(Vector(x, y))
        if (history.size > trailCount) {
            history.removeAt(0)
        }


        //apply acceleration
        xVelocity *= acceleration + (size / 100)
        yVelocity *= acceleration + (size / 100)


        //apply velocity
        x += xVelocity * deltaTime * FRAME_RATE
        y += yVelocity * deltaTime * FRAME_RATE


        //increase the size overtime
        size += 0.1f


        //check if the particle is out of bounds
        if ((x > w || x < 0) || (y > h || y < 0)) {

            //reset particle and reposition inside the bounds
            resetParticle(w, h)
        }
    }

    /**
     * resets the parameters of the a particle
     * */
    private fun resetParticle(w: Int, h: Int) {
        val centerX = w / 2f
        val centerY = h / 2f

        val x = Random.nextDouble(0.0, w.toDouble()).toFloat()
        val y = Random.nextDouble(0.0, h.toDouble()).toFloat()

        val xVelocity = ((x - centerX) / centerX)
        val yVelocity = ((y - centerY) / centerX)


        this.history.clear()
        this.size = 0.5f
        this.x = x
        this.y = y
        this.xVelocity = xVelocity
        this.yVelocity = yVelocity

    }

    /**
     * stop the particle
     * */
    fun stop() {
        history.clear()
    }

    companion object {

        fun newStarInstance(w: Int, h: Int): SpaceTravelStarParticle {
            val centerX = w / 2f
            val centerY = h / 2f

            val x = Random.nextDouble(0.0, w.toDouble()).toFloat()
            val y = Random.nextDouble(0.0, h.toDouble()).toFloat()

            val xVelocity = ((x - centerX) / centerX)
            val yVelocity = ((y - centerY) / centerY)


            return SpaceTravelStarParticle(
                x = x,
                y = y,
                xVelocity = xVelocity,
                yVelocity = yVelocity,
                acceleration = 1.2f,
                history = mutableListOf(),
                size = Random.nextDouble(0.5, 1.0).toFloat(),
                alpha = 1f,
                paint = Paint().apply { color = Color.WHITE })
        }


    }


}