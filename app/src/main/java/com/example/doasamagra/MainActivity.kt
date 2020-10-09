package com.example.doasamagra

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.doasamagra.qiblacompass.QiblaCompassFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
                } else {
                    Log.d("MaiActivity", "Location is null")
                }
            }
        }
    }

    private fun onGetLocationSuccess(location: Location) {
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
            //TODO compass system not available
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
                    //TODO: Location permission denied
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }//case end

    }
}
