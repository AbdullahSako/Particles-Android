package com.sako.sparkletest

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class SparkleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val particleCount = 1000
    private val particleList = mutableListOf<Particle>()
    private val particleColor = Color.GREEN
    private val randomColor = true
    private val enableTopFadingEdge = true
    private val enableBottomFadingEdge = false
    private val enableLeftFadingEdge = false
    private val enableRightFadingEdge = false
    private var enableTouchToAccelerate = true
    


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        repeat(particleCount) {
            particleList.add(
                Particle(
                    x = Random.nextDouble(0.0, w.toDouble()).toFloat(),
                    y = Random.nextDouble(0.0, h.toDouble()).toFloat(),
                    xVelocity = Random.nextDouble(-0.5, 0.5).toFloat(),
                    yVelocity = Random.nextDouble(-0.5, 0.5).toFloat(),
                    acceleration = 1.0f,
                    size = Random.nextDouble(1.0, 3.0).toFloat(),
                    alpha = Random.nextFloat(),
                    alphaChangeValue = Random.nextDouble(0.0, 0.03).toFloat(),
                    paint = Paint().apply {
                        color = if (randomColor) {
                            generateRandomColor()
                        } else {
                            particleColor
                        }
                    })
            )
        }

        startAnimation()
    }

    private fun startAnimation() {

        val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.interpolator = LinearInterpolator()
        animator.duration = 5000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.RESTART
        animator.addUpdateListener {
            postInvalidateOnAnimation()
        }

        animator.start()


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val particle = particleList[0]
//        Log.d("TESTLOG","(${particle.xVelocity},${particle.yVelocity})")
//        Log.d("TESTLOG","(${particle.acceleration})")

        repeat(particleCount) {
            canvas.drawCircle(
                particleList[it].x,
                particleList[it].y,
                particleList[it].size,
                particleList[it].paint
            )
            particleList[it].move(width, height)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (enableTouchToAccelerate) {
            if (event?.action == MotionEvent.ACTION_DOWN) {
                for (particle in particleList) {
                    val distanceBetweenTwoPoints =
                        sqrt((particle.x - event.x).pow(2) + (particle.y - event.y).pow(2))
                    if (distanceBetweenTwoPoints < 300f) {
                        particle.accelerateAwayFrom(event.x, event.y, distanceBetweenTwoPoints)
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun generateRandomColor(): Int {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return Color.rgb(red, green, blue)
    }

}