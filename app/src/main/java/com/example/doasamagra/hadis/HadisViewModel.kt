package com.example.doasamagra.hadis

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class HadisViewModel : ViewModel() {
    private val hadisCollection = FirebaseFirestore.getInstance().collection("hadis")
    private val _hadis = MutableLiveData<List<Hadis>>()
    val hadis: LiveData<List<Hadis>> = _hadis

    fun loadData(cb: (() -> Unit)? = null) {
        hadisCollection.orderBy("ref").get()
            .addOnSuccessListener { documents ->
                _hadis.value = documents.toObjects(Hadis::class.java)
                cb?.let { it() }
            }.addOnFailureListener { e ->
                Log.d("HadisViewModel", "There is some problems loading the data")
                cb?.let { it() }
            }
    }
}