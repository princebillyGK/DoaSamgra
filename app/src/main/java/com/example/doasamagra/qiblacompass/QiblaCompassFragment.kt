package com.example.doasamagra.qiblacompass

import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doasamagra.R
import com.example.doasamagra.databinding.QiblaCompassFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class QiblaCompassFragment : Fragment(), SensorEventListener, LocationListener {

    private lateinit var viewModel: QiblaCompassViewModel
    private lateinit var binding: QiblaCompassFragmentBinding

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManger: LocationManager

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private var compassFlag: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationManger = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        getLocation()
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
        viewModel = ViewModelProvider(this).get(QiblaCompassViewModel::class.java)

        viewModel.dialRotation.observe(
            viewLifecycleOwner,
            Observer { newDialRotation -> binding.dial.rotation = newDialRotation })
        viewModel.qiblaArrowRotation.observe(
            viewLifecycleOwner,
            Observer { newQiblaArrowRotation ->
                binding.qiblaArrow.rotation = newQiblaArrowRotation
            }
        )
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            Log.d("MainActivity", "getLocation: permission granted")
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.e("QiblaCompassFragment", "Last location time ${location.time}")
                    onGetLocationSuccess(location)
                } else if (LocationManagerCompat.isLocationEnabled(locationManger)) {
                    binding.gpsLoading.visibility = View.VISIBLE
                    locationManger.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0L,
                        0f,
                        this
                    )
                } else {
                    Toast.makeText(requireActivity(), "GPS is turned off", Toast.LENGTH_SHORT).show()
                    binding.tryAgain.setOnClickListener {
                        getLocation()
                        binding.gpsEnableOption.visibility = View.GONE
                    }
                    binding.gpsEnableOption.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onGetLocationSuccess(location: Location) {
        if (accelerometer != null && magnetometer != null) {
            compassFlag = true
            viewModel.setLocation(location)
            setSensorListeners()
            binding.compassLayout.visibility = View.VISIBLE
        } else {
            binding.compassMessage.text = resources.getString(R.string.compass_unavailable_text)
            binding.compassMessage.visibility = View.VISIBLE
        }
    }

    override fun onLocationChanged(location: Location) {
        /** Stops location after receiving fast signal */
        onGetLocationSuccess(location)
        binding.gpsLoading.visibility = View.GONE
        locationManger.removeUpdates(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //case start
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                // if location is granted
                if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    Toast.makeText(this.activity, "Location Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                    binding.tryAgain.setOnClickListener { getLocation() }
                    binding.compassMessage.text =
                        resources.getString(R.string.gps_permission_disabled_text)
                    binding.compassMessage.visibility = View.VISIBLE
                }
            }
        }//case end

    }

    override fun onResume() {
        super.onResume()
        if (compassFlag) {
            setSensorListeners()
        }
    }

    private fun setSensorListeners() {
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