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
val speed : Float = 0.025f

fun Canvas.drawSSINode(i : Int, scale : Float,cb : () -> Unit, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w * 0.8f / nodes
    val deg : Float = 360f / nodes
    val size : Float = gap / 4
    paint.strokeWidth = Math.min(w, h) / 50
    paint.strokeCap = Paint.Cap.ROUND
    paint.color = Color.parseColor("#673AB7")
    val sc1 : Float = Math.min(0.5f, scale) * 2
    val sc2 : Float = Math.min(0.5f, Math.max(0f, scale - 0.5f)) * 2
    val x : Float = (size/2) / (Math.tan((deg / 2) * (Math.PI / 180)).toFloat())
    save()
    translate(gap * i + gap / 2 + gap * sc2, h / 2)
    rotate(i * deg)
    drawLine(x, -size / 2, x, -size / 2 + 2 * size * sc1, paint)
    restore()
    save()
    translate(gap * sc2, 0f)
    cb()
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
            scale += speed * dir
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

    data class SSINode(var i : Int, val state : State = State()) {

        private var next : SSINode? = null

        private var prev : SSINode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = SSINode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawSSINode(i, state.scale, {
                prev?.draw(canvas, paint)
            }, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : SSINode {
            var curr : SSINode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class LinkedSSI(var i : Int) {

        private var curr : SSINode = SSINode(0)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            curr.update {i, scale ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(i, scale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }
}