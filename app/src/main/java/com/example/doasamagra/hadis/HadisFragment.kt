package com.example.doasamagra.hadis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.doasamagra.R
import com.example.doasamagra.databinding.HadisFragmentBinding
import com.example.doasamagra.surahplayer.Surah

class HadisFragment : Fragment() {

    companion object {
        fun newInstance() = HadisFragment()
    }

    private lateinit var viewModel: HadisViewModel
    private lateinit var binding: HadisFragmentBinding
    private lateinit var hadisItemAdapter: HadisItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.hadis_fragment, container, false)
        hadisItemAdapter = HadisItemAdapter()
        hadisItemAdapter.data = listOf<Hadis>()
        binding.hadisList.adapter = hadisItemAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HadisViewModel::class.java)
        viewModel.hadis.observe(viewLifecycleOwner, Observer{
            it?.let {
                hadisItemAdapter.data = it
            }
        })
        viewModel.loadData()
    }
}