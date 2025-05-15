package com.sako.particles_compose.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

data class SparklesParticle(
    var x: Float,
    var y: Float,
    var xVelocity: Float,
    var yVelocity: Float,
    val maxVelocity: Float,
    var acceleration: Float,
    val maxAccel: Float,
    val size: Float,
    var alpha: Float,
    var alphaChangeValue: Float,
    var color: Color
) {

    /**
     * Move the particle based on its velocity and acceleration
     *
     *  @param viewWidth Canvas view width
     *  @param viewHeight Canvas view height
     * */
    fun move(viewWidth: Int, viewHeight: Int) {

        normalizeVelocity()

        changeDirectionIfOutOfBounds(viewWidth, viewHeight)

        flickerParticle()

        //normalize acceleration overtime
        if (acceleration > 1f) {
            acceleration = max(acceleration - 0.01f, 1f)
        } else {
            acceleration = min(acceleration + 0.01f, 1f)
        }

        //apply acceleration to the velocity
        xVelocity *= acceleration
        yVelocity *= acceleration


        //move the particle
        x += xVelocity
        y += yVelocity

    }


    /**
     * Normalizes velocity value slowly on each draw to the specified velocity range
     * */
    private fun normalizeVelocity(){

        //normalize x-velocity overtime in case of acceleration change
        if (xVelocity > maxVelocity) {
            xVelocity -= 0.05f
        } else if (xVelocity < maxVelocity*-1) {
            xVelocity += 0.05f
        }

        //normalize y-velocity overtime in case of acceleration change
        if (yVelocity > maxVelocity) {
            yVelocity -= 0.05f
        } else if (yVelocity < maxVelocity*-1) {
            yVelocity += 0.05f
        }

    }

    /**
     * Changes the direction of the particle if the next move would be out of bounds (Edges of the view)
     * */
    private fun changeDirectionIfOutOfBounds(width: Int, height: Int){

        //if the particle reached the left edge or the right edge of the view, reverse the velocity
        if (x + xVelocity > width || x + xVelocity < 0) {
            xVelocity *= -1
        }

        //if the particle reached the top edge or the bottom edge of the view, reverse the velocity
        if (y + yVelocity > height || y + yVelocity < 0) {
            yVelocity *= -1
        }
    }



    /**
     * Changes the alpha of the particle as it moves through the view, so it flickers as it moves
     * */
    private fun flickerParticle(){
        //if the alpha is 1 or 0, reverse the alphaChangeValue so the particle keeps fading/appearing
        if (alpha >= 1f || alpha <= 0f) {
            alphaChangeValue *= -1
        }

        //change the alpha of the particle as it moves through the view
        color = color.copy(alpha = this.alpha)
        alpha = (alpha + alphaChangeValue).coerceIn(0f, 1f)
    }



    //-------------------------- ACCEL -------------------------------------

    /**
     * Accelerate away from the provided x,y point
     * This method changes the acceleration variable for the particle so the particle will accelerate away from the x,y point on the next move() method call
     * */
    fun accelerateAwayFrom(x: Float, y: Float, distance: Float) {

        //change velocity so the particle accelerates away from the x,y point
        if (x > this.x && y < this.y) { //top left of the x,y point
            this.yVelocity = Random.nextDouble(0.1, maxVelocity.toDouble()).toFloat()
            this.xVelocity = Random.nextDouble(maxVelocity.toDouble()*-1, -0.1).toFloat()
        } else if (x < this.x && y < this.y) {//top right of the x,y point
            this.yVelocity = Random.nextDouble(0.1, maxVelocity.toDouble()).toFloat()
            this.xVelocity = Random.nextDouble(0.1, maxVelocity.toDouble()).toFloat()
        } else if (x > this.x && y > this.y) {//bottom left of the x,y point
            this.yVelocity = Random.nextDouble(maxVelocity.toDouble()*-1, -0.1).toFloat()
            this.xVelocity = Random.nextDouble(maxVelocity.toDouble()*-1, -0.1).toFloat()

        } else {//bottom right of the x,y point
            this.yVelocity = Random.nextDouble(maxVelocity.toDouble()*-1, -0.1).toFloat()
            this.xVelocity = Random.nextDouble(0.1, maxVelocity.toDouble()).toFloat()
        }


        //change the acceleration
        this.acceleration = calculateAccelerationBasedOnDistance(
            distance,
            300f,
            1.1f,
            maxAccel
        )
    }

    /**
     * Calculates acceleration based on the distance of the particle that will be accelerated from
     * This method returns greater acceleration as the distance lowers
     * */
    private fun calculateAccelerationBasedOnDistance(
        distance: Float,
        maxDistance: Float = 300f,
        minAccel: Float,
        maxAccel: Float
    ): Float {
        val normalizedDistance = distance / maxDistance
        val accelerationRange = maxAccel - minAccel
        return maxAccel - (normalizedDistance * accelerationRange)
    }

}