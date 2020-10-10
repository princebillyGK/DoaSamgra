package com.example.doasamagra

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.databinding.DataBindingUtil
import com.example.doasamagra.databinding.ActivityMainBinding
import com.example.doasamagra.qiblacompass.QiblaCompassFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityMainBinding
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManger: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationManger = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        getLocation()
    }


    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            Log.d("MainActivity", "getLocation: permission granted")
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.e("MainActivity", "Last location time ${location.time}")
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
                    Toast.makeText(this, "GPS is turned off", Toast.LENGTH_SHORT).show()
                    binding.tryAgain.setOnClickListener{
                        getLocation()
                        binding.gpsEnableOption.visibility = View.GONE
                    }
                    binding.gpsEnableOption.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onGetLocationSuccess(location: Location) {

        //init Qibla Compass if sensor available
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if (accelerometer != null && magnetometer != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(
                R.id.fragment_container,
                QiblaCompassFragment.newInstance(location)
            )
                .addToBackStack(null).commit()
        } else {
            binding.compassMessage.text = resources.getString(R.string.compass_unavailable_text)
            binding.compassMessage.visibility = View.VISIBLE
        }
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
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                    binding.tryAgain.setOnClickListener { getLocation() }
                    binding.compassMessage.text =
                        resources.getString(R.string.gps_permission_disabled_text)
                    binding.compassMessage.visibility = View.VISIBLE
                }
            }
        }//case end

    }

    override fun onLocationChanged(location: Location) {
        if (location != null) {
            onGetLocationSuccess(location)
            binding.gpsLoading.visibility = View.GONE
            locationManger.removeUpdates(this)
        }
    }
}
