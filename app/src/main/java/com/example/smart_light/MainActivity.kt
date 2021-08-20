package com.example.smart_light

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat

const val DEVICE_MAC = "MAC address here"

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var howBright: Sensor? = null
    private lateinit var textViewALS : TextView
    private var ble: BLEControl? = null
    private var messages: TextView? = null
    private var rssiAverage:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  //Initiallizes us in light and not night mode
        textViewALS = findViewById(R.id.textViewALS)

        setUpALSSensor()
        val adapter: BluetoothAdapter?
        adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter != null) {
            if (!adapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

            }        }

        // Get Bluetooth
        messages = findViewById(R.id.bluetoothText)
        messages!!.movementMethod = ScrollingMovementMethod()
        ble = BLEControl(applicationContext, DEVICE_NAME)

        // Check permissions
        ActivityCompat.requestPermissions(this,
            arrayOf( Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE), 1)






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