package com.sako.particles.model.firework

import android.graphics.Paint
import android.graphics.Rect
import com.sako.particles.model.Vector
import com.sako.particles.utils.Tools.logd
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

data class FireworkExplosionParticle(
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
    private val gravity = 0.03f //Gravity value

    fun move(trailLength:Int, distanceLimitRectangle: Rect,enableSmudge:Boolean = false) {
        val historyCount = history.count()


        //normalize velocity overtime
        normalizeVelocity()

        //Apply the alpha to the paint
        val newAlpha = (calculateAlphaBasedOnRectangleArea(distanceLimitRectangle) * 255).toInt()
        if(newAlpha != paint.alpha){
            paint.alpha =newAlpha
        }


        //normalize acceleration overtime
        if (acceleration > 1f) {
            acceleration = max(acceleration - 0.01f, 1f)
        } else {
            acceleration = min(acceleration + 0.01f, 1f)
        }

        //apply gravity to the velocity
        yVelocity += gravity

        //apply acceleration to the velocity
        xVelocity *= acceleration
        yVelocity *= acceleration


        //add the current position to the history
        if(enableSmudge) {

            if (historyCount < trailLength) {
                history.add(Vector(x, y))
            }

        }else{
            history.add(Vector(x, y))

             if(history.size > trailLength) {
                history.removeAt(0)
            }

        }

        //move the particle
        x += xVelocity
        y += yVelocity

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
     * 1) distance of the particle from given rectangle
     * 2) the life span of the particle
     * */
    private fun calculateAlphaBasedOnRectangleArea(rect: Rect): Float {


        //check if the particle is inside the rectangle
        val distanceAlpha = if(x > rect.left && x < rect.right && y > rect.top && y < rect.bottom) {

            // Calculate the distance to the edges
            val distanceToLeft = (x - size) - rect.left
            val distanceToRight = rect.right - (x + size)
            val distanceToTop = (y - size) - rect.top
            val distanceToBottom = rect.bottom - (y + size)


            // Find the minimum distance to any edge
            val minDistance = minOf(
                distanceToLeft,
                distanceToRight,
                distanceToTop,
                distanceToBottom
            ).absoluteValue


            if (minDistance > fadeDistance) {
                1f // Fully opaque
            } else {
                (minDistance / fadeDistance).coerceIn(0f, 1f)
            }

        }else{
            //if the particle is outside the rectangle, return alpha as 0f
            0f
        }

        // Calculate alpha based on life
        val lifeAlpha = ((lifeSpan.toFloat()/maxLifeSpan)).coerceIn(0f, 1f)


        // Take the minimum of the two alphas
        return minOf(distanceAlpha, lifeAlpha)

    }

}



