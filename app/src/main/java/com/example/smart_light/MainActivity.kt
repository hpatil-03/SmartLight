package com.example.smart_light

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var howBright: Sensor? = null
    private lateinit var textViewALS : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  //Initiallizes us in light and not night mode
        textViewALS = findViewById(R.id.textViewALS)

        setUpALSSensor()
    }

    private fun setUpALSSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        howBright = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }
    
    // This function detects the sensor events
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light = event.values[0]
            textViewALS.text = brightness(light)
        }
    }

    //Not using the following method
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    private fun brightness(brightness:Float):String {
        return brightness.toInt().toString()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, howBright,SensorManager.SENSOR_DELAY_NORMAL)

    }

    // The following function keep the listener from running without the screen on, we may want to get rid of this
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}