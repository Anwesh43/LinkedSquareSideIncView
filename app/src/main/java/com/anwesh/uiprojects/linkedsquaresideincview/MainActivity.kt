package com.anwesh.uiprojects.linkedsquaresideincview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.squaresideincview.SquareSideIncView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SquareSideIncView.create(this)
    }
}
