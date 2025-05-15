package com.sako.particles_compose.utils

import android.util.Log
import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

object Tools {

    /**
     * Generates a random color
     * */
     fun generateRandomColor(): Color {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return Color(red, green, blue)
    }

    /**
     * sets the alpha of a color
     * */
     fun Int.setColorAlpha(@IntRange(0, 255) alpha: Int): Color {
        return Color(this.red, this.green, this.blue,alpha)
    }

    /**
     * Calculates the distance between two points
     * */
    fun distanceBetweenTwoPoints(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
    }

    /**
     * Log extension
     * */
    fun Any.logd(preFix: String = "") {
        Log.d("TESTLOG", "$preFix $this")
    }

}