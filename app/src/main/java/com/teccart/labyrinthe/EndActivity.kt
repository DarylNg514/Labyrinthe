package com.teccart.labyrinthe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EndActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val endScreen = EndScreen(this)
        //startScreen.setKeepScreenOn(true);
        setContentView(endScreen)
    }
}