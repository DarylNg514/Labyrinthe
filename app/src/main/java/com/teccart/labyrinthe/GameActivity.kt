package com.teccart.labyrinthe

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var labyrintheView: labyrinthe
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor

    var mode:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        labyrintheView = labyrinthe(this,mode)
        setContentView(labyrintheView)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometerSensor == null) {
            // Handle the case where the device does not have an accelerometer
            finish() // Close the app or alert the user
        }
    }


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (mode==true)
        {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = -event.values[0]  // Inverting X axis for natural movement
                val y = event.values[1]  // Y axis is used as is

                // Convert accelerometer readings into screen coordinate movements
                val dx = (x * 5).toInt()  // Scale factor for sensitivity, adjust as needed
                val dy = (y * 5).toInt()

                // Move the ball based on the accelerometer data
                labyrintheView.moveBall(dx, dy)
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int)
    {

    }


}