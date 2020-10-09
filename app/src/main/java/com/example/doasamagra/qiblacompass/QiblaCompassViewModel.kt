package com.example.doasamagra.qiblacompass

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.PI

object KaabaLocation : Location("") {
    init {
        latitude = 21.4225
        longitude = 39.8262
    }
}

class QiblaCompassViewModel(private val location: Location) : ViewModel() {
    private var accelerometerData = FloatArray(3)
    private var magnetometerData = FloatArray(3)
    val dialRotation = MutableLiveData<Float>(0f)
    val qiblaArrowRotation = MutableLiveData<Float>(0f)

    init {

    }

    fun listenSensorChange(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> accelerometerData = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> magnetometerData = event.values.clone()
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
        val compassRotation = -azimuth * 180 / PI.toFloat()
        dialRotation.value = compassRotation
        qiblaArrowRotation.value = compassRotation + location.bearingTo(KaabaLocation)
    }
}