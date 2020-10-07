package com.example.doasamagra.compass

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.doasamagra.R
import com.example.doasamagra.databinding.FragmentCompassBinding

class CompassFragment : Fragment(), SensorEventListener {
    private lateinit var binding: FragmentCompassBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var magnetometer: Sensor

    private lateinit var accelerometerData: FloatArray
    private lateinit var magnetometerData: FloatArray


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compass, container, false)
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //getting accelerometer
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            ?.let { sensor -> accelerometer = sensor } ?: run {
            binding.compassMessage.text = resources.getString(R.string.compass_sensor_null_error)
        }
        //getting magnetometer
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            ?.let { sensor -> magnetometer = sensor } ?: run {
            binding.compassMessage.text = resources.getString(R.string.compass_sensor_null_error)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (::accelerometer.isInitialized and ::magnetometer.isInitialized) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> accelerometerData = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> magnetometerData = event.values.clone()
            else -> return
        }

        val rotationMatrix = FloatArray(9)
        val rotationOk = SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerData,
            magnetometerData
        )

        val orientationValue = FloatArray(3)
        if (rotationOk) {
            SensorManager.getOrientation(rotationMatrix, orientationValue)
        }
        val (azimuth, pitch, roll) = orientationValue

        binding.compassMessage.text = "Azimuth: $azimuth, Pitch $pitch, Roll, $roll"
    }
}