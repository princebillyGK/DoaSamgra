package com.example.doasamagra.qiblacompass

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doasamagra.R
import com.example.doasamagra.databinding.QiblaCompassFragmentBinding

class QiblaCompassFragment(
    private val location: Location
) : Fragment(), SensorEventListener {
    /** Creates a compass instance */

    companion object {
        fun newInstance(
            location: Location
        ): QiblaCompassFragment =
            QiblaCompassFragment(location)
    }

    private lateinit var viewModel: QiblaCompassViewModel
    private lateinit var viewModelFactory: QiblaCompassViewModelFactory
    private lateinit var binding: QiblaCompassFragmentBinding


    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var magnetometer: Sensor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.qibla_compass_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelFactory = QiblaCompassViewModelFactory(location)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(QiblaCompassViewModel::class.java)

        viewModel.dialRotation.observe(
            viewLifecycleOwner,
            Observer { newDialRotation -> binding.dial.rotation = newDialRotation })
        viewModel.qiblaArrowRotation.observe(
            viewLifecycleOwner,
            Observer { newQiblaArrowRotation -> binding.qiblaArrow.rotation = newQiblaArrowRotation }
        )
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //nothing
    }

    override fun onSensorChanged(event: SensorEvent?) {
        viewModel.listenSensorChange(event)
    }
}