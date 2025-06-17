package com.sako.particles_compose.ui.sparkles

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.sako.particles_compose.model.SparklesParticle
import com.sako.particles_compose.utils.Tools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun SparklesView(
    modifier: Modifier = Modifier,
    particlesCount: Int = 150,
    particlesColor: Color = Color.Green,
    randomColor: Boolean = false,
    enableTouchToAccelerate: Boolean = true,
    maxVelocity: Float = 0.5f,
    maxAcceleration: Float = 1.33f,
    maxSize: Float = 3f
) {
    val density = LocalDensity.current
    val particles = remember { mutableStateListOf<SparklesParticle>() }
    val coroutineScope = rememberCoroutineScope()

    //force recompose of canvas on each frame
    val reComposeSignal = remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { frameTime ->
                reComposeSignal.longValue = frameTime
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier.run {
            //--------enable touch to accelerate-----------
            if (enableTouchToAccelerate) {
                pointerInput(Unit) {
                    detectTapGestures(onTap = { offset: Offset ->
                        coroutineScope.launch(Dispatchers.Default) {
                            for (particle in particles) {
                                //calculate the distance between the current particle and the touch point
                                val distanceBetweenTwoPoints =
                                    sqrt(
                                        (particle.x - offset.x).pow(2) + (particle.y - offset.y).pow(
                                            2
                                        )
                                    )

                                //if the distance is more than 300, accelerate the particle away from the touch point
                                if (distanceBetweenTwoPoints < 300f) {
                                    //accelerate the particle away from the touch point
                                    particle.accelerateAwayFrom(
                                        offset.x,
                                        offset.y,
                                        distanceBetweenTwoPoints
                                    )
                                }
                            }
                        }
                    })
                }
            } else {
                this
            }
        }
    ) {
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        // Initialize particles once
        LaunchedEffect(Unit) {
            particles.clear()
            repeat(particlesCount) {
                particles.add(
                    SparklesParticle(
                        x = Random.nextDouble(0.0, widthPx.toDouble()).toFloat(),
                        y = Random.nextDouble(0.0, heightPx.toDouble()).toFloat(),
                        xVelocity = Random.nextDouble(
                            maxVelocity.toDouble() * -1,
                            maxVelocity.toDouble()
                        ).toFloat(),
                        yVelocity = Random.nextDouble(
                            maxVelocity.toDouble() * -1,
                            maxVelocity.toDouble()
                        ).toFloat(),
                        maxVelocity = maxVelocity,
                        acceleration = 1.0f,
                        maxAccel = maxAcceleration,
                        size = Random.nextDouble(1.0, maxSize.toDouble()).toFloat(),
                        alpha = Random.nextFloat(),
                        alphaChangeValue = Random.nextDouble(0.0, 0.03).toFloat(),
                        color = if (randomColor) {
                            Tools.generateRandomColor()
                        } else {
                            particlesColor
                        }
                    )
                )
            }
        }


        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //causes canvas to redraw
            reComposeSignal.longValue

            // Draw particles
            particles.forEach {
                //draw particle
                drawCircle(
                    color = it.color,
                    radius = it.size,
                    center = Offset(it.x, it.y)
                )


                //move particle
                it.move(widthPx.toInt(), heightPx.toInt())


                //------Draw connect lines-------
//                if (enableParticleConnect) { //check if flag is enables
//
//
//                        //iterate through all particles to check if the current particle drawn is within distance with any other particle
//                        for (particleTo in particles) {
//
//                            //check if the current particle is not the same as the particle being iterated through
//                            if (particleTo != it) {
//
//                                //calculate the distance between the current particle and the particle being iterated through
//                                val distanceBetweenParticles = distanceBetweenTwoPoints(
//                                    it.x,
//                                    it.y,
//                                    particleTo.x,
//                                    particleTo.y
//                                )
//
//                                //if within distance, draw a line between the two particles
//                                if (distanceBetweenParticles < maxParticleConnectDistance) {
//                                        drawLine(
//                                            connectLineColor,
//                                            Offset(it.x, it.y),
//                                            Offset(particleTo.x, particleTo.y)
//                                        )
//                                }
//                            }
//                        }
//
//                }

            }


        }
    }
}
