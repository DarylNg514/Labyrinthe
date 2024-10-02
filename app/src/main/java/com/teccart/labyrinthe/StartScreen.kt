package com.teccart.labyrinthe

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View

class StartScreen( context: Context) : View(
    context
) {
    private val screenWidth = context.resources.displayMetrics.widthPixels
    private val screenHeight = context.resources.displayMetrics.heightPixels
    private val startPageLogo: Bitmap
    private val btnStartUp: Bitmap
    private val btnStartDown: Bitmap
    private var playBtnState = false

    init {
        startPageLogo = BitmapFactory.decodeResource(resources, R.drawable.ac3)
        btnStartUp = BitmapFactory.decodeResource(resources, R.drawable.playbutton)
        btnStartDown = BitmapFactory.decodeResource(resources, R.drawable.playbutton2)
    }

    override fun onDraw(canvas: Canvas) {
         super.onDraw(canvas);
        val scaledBitmap = Bitmap.createScaledBitmap(startPageLogo, screenWidth, screenHeight, true)

        // Now draw the scaled bitmap
        canvas.drawBitmap(scaledBitmap, 0f, 0f, null)
        if (playBtnState) {
            canvas.drawBitmap(
                btnStartDown, ((screenWidth - btnStartDown.width) / 2).toFloat(),
                (screenHeight * 0.75).toInt().toFloat(), null
            )
        } else {
            canvas.drawBitmap(
                btnStartUp,
                (
                    (screenWidth - btnStartUp.width) / 2).toFloat(),
                    (screenHeight * 0.75).toInt().toFloat(), null
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // return super.onTouchEvent(event);
        val action = event.action
        val touchX = event.x.toInt()
        val touchY = event.y.toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> if (touchX > (screenWidth - btnStartUp.width) / 2 &&
                touchX < (screenWidth - btnStartUp.width) / 2 +
                btnStartUp.width && touchY > (screenHeight * 0.75).toInt() && touchY < (screenHeight * 0.75).toInt() + btnStartUp.height
            ) {
                playBtnState = true
            }

            MotionEvent.ACTION_UP -> {
                if (playBtnState) {
                    val gameIntent = Intent(context, GameActivity::class.java)
                    context.startActivity(gameIntent)
                }
                playBtnState = false
            }

            MotionEvent.ACTION_MOVE -> {}
        }
        invalidate()
        return true
    }

}