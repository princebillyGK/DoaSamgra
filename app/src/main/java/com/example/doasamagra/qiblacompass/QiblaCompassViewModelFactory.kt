package com.example.doasamagra.qiblacompass

import android.hardware.Sensor
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


class QiblaCompassViewModelFactory (private val location: Location): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((QiblaCompassViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return QiblaCompassViewModel(location) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}