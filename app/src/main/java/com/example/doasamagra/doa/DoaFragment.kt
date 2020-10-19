package com.example.doasamagra.doa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.doasamagra.R
import com.example.doasamagra.databinding.DoaFragmentBinding

class DoaFragment : Fragment() {

    companion object {
        fun newInstance() = DoaFragment()
    }

    private lateinit var viewModel: DoaViewModel
    private lateinit var binding: DoaFragmentBinding
    private lateinit var doaItemAdapter: DoaItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.doa_fragment, container, false)
        doaItemAdapter = DoaItemAdapter()
        doaItemAdapter.data = listOf<Doa>()
        binding.doaList.adapter = doaItemAdapter
        binding.doaSwipeRefresh.setOnRefreshListener {
            loadData()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DoaViewModel::class.java)
        viewModel.doa.observe(viewLifecycleOwner, Observer {
            it?.let {
                doaItemAdapter.data = it
            }
        })
        loadData()
    }

    private fun loadData() {
        if (!binding.doaSwipeRefresh.isRefreshing) binding.doaSwipeRefresh.isRefreshing = true
        viewModel.loadData {
            binding.doaSwipeRefresh.isRefreshing = false
        }
    }
}