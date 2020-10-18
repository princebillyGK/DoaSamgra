package com.example.doasamagra.doa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class DoaViewModel : ViewModel() {
    private val doaCollection = FirebaseFirestore.getInstance().collection("doa")
    private val _doa = MutableLiveData<List<Doa>>()
    val doa: LiveData<List<Doa>> = _doa

    fun loadData() {
        doaCollection.orderBy("title").get()
            .addOnSuccessListener { documents ->
                _doa.value = documents.toObjects(Doa::class.java)
            }.addOnFailureListener { e ->
                Log.d("DoaViewModel", "There is some problems loading the data")
            }
    }
}
