package com.sako.particles.model

import android.graphics.Paint
import kotlin.math.max
import kotlin.math.min
import kotlin.ranges.coerceIn

data class ExplosionParticle(
    var x: Float,
    var y: Float,
    var xVelocity: Float,
    var yVelocity: Float,
    val maxVelocity: Float,
    var acceleration: Float,
    var size: Float,
    val history: MutableList<Vector>,
    val paint: Paint,
    var lifeSpan: Int = 150,
    val maxLifeSpan: Int = 150
) {
    private val fadeDistance = 100f //Distance from the edge where fading starts

    fun move(width: Int, height: Int,trailLength:Int) {

        //normalize velocity overtime
        normalizeVelocity()

        //Apply the alpha to the paint
        paint.alpha = (calculateAlpha(width,height) * 255).toInt()

        //normalize acceleration overtime
        if (acceleration > 1f) {
            acceleration = max(acceleration - 0.01f, 1f)
        } else {
            acceleration = min(acceleration + 0.01f, 1f)
        }

        //apply acceleration to the velocity
        xVelocity *= acceleration
        yVelocity *= acceleration


        //add the current position to the history
        history.add(Vector(x,y))

        //move the particle
        x += xVelocity
        y += yVelocity

        //limit the trail list to 10 items
        if(history.count()>trailLength){
            history.removeAt(0)
        }

        //decrease the life span of the particle
        lifeSpan--
    }


    /**
     * Normalizes velocity value slowly on each draw to the specified velocity range
     * */
    private fun normalizeVelocity() {

        //normalize x-velocity overtime in case of acceleration change
        if (xVelocity > maxVelocity) {
            xVelocity -= 0.1f
        } else if (xVelocity < maxVelocity * -1) {
            xVelocity += 0.1f
        }

        //normalize y-velocity overtime in case of acceleration change
        if (yVelocity > maxVelocity) {
            yVelocity -= 0.1f
        } else if (yVelocity < maxVelocity * -1) {
            yVelocity += 0.1f
        }

    }

    /**
     * calculate the alpha based two factors:
     * 1) distance of the particle from the edge of the view
     * 2) the life span of the particle
     * */
    private fun calculateAlpha(width:Int,height: Int): Float {

        // Calculate the distance to the edges
        val distanceToLeft = x - size
        val distanceToRight = width - (x + size)
        val distanceToTop = y - size
        val distanceToBottom = height - (y + size)

        // Find the minimum distance to any edge
        val minDistance = minOf(distanceToLeft, distanceToRight, distanceToTop, distanceToBottom)


        val distanceAlpha = if (minDistance > fadeDistance) {
            1f // Fully opaque
        } else {
            (minDistance / fadeDistance).coerceIn(0f, 1f)
        }


        // Calculate alpha based on life
        val lifeAlpha = ((lifeSpan.toFloat()/maxLifeSpan)).coerceIn(0f, 1f)


        // Take the minimum of the two alphas
        return minOf(distanceAlpha, lifeAlpha)

    }

}

