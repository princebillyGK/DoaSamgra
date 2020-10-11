package com.example.doasamagra.doa

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.doasamagra.R

class DoaFragment : Fragment() {

    companion object {
        fun newInstance() = DoaFragment()
    }

    private lateinit var viewModel: DoaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.doa_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DoaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}