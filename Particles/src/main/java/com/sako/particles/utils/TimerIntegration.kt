package com.sako.particles.utils



/**
 * https://github.com/DanielMartinus/Konfetti
 *
 * TimerIntegration retrieves the delta time since the rendering of the previous frame.
 * Delta time is used to draw the confetti correctly if any frame drops occur.
 */
class TimerIntegration {
    private var previousTime: Long = -1L

    fun reset() {
        previousTime = -1L
    }

    fun getDeltaTime(): Float {
        if (previousTime == -1L) previousTime = System.nanoTime()

        val currentTime = System.nanoTime()
        val dt = (currentTime - previousTime) / 1000000f
        previousTime = currentTime
        return dt / 1000
    }

    fun getTotalTimeRunning(startTime: Long): Long {
        val currentTime = System.currentTimeMillis()
        return (currentTime - startTime)
    }
}