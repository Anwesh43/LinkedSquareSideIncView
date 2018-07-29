package com.anwesh.uiprojects.squaresideincview

/**
 * Created by anweshmishra on 30/07/18.
 */

import android.view.View
import android.content.Context
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

val nodes : Int = 4

fun Canvas.drawSSINode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w * 0.8f / nodes
    val deg : Float = 360f / nodes
    val size : Float = gap / 4
    paint.strokeWidth = Math.min(w, h) / 50
    paint.strokeCap = Paint.Cap.ROUND
    paint.color = Color.parseColor("#673AB7")
    val x : Float = (size/2) / (Math.tan((deg / 2) * (Math.PI / 180)).toFloat())
    save()
    translate(gap * i + gap / 2, h / 2)
    rotate(i * deg)
    drawLine(x, -size / 2, x, size / 2, paint)
    restore()
}

class SquareSideIncView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += 0.1f * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }
}