package com.example.exo2_v2

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null
    private lateinit var temperatureTextView: TextView
    private lateinit var colorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        temperatureTextView = findViewById(R.id.temperatureTextView)
        colorTextView = findViewById(R.id.colorTextView)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    }

    override fun onResume() {
        super.onResume()
        temperatureSensor?.also { temp ->
            sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                val temperature = it.values[0]
                temperatureTextView.text = getString(R.string.temperature_value, temperature)
                val color = interpolateColor(temperature)
                colorTextView.setBackgroundColor(color)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // nothing here
    }

    private fun interpolateColor(temperature: Float): Int {
        val normalizedTemperature = (temperature + 10) / 30
        return ColorUtils.blendARGB(Color.BLUE, Color.RED, normalizedTemperature)
    }
}
