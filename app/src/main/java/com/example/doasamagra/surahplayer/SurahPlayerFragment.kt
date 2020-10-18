package com.example.doasamagra.surahplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.doasamagra.R
import com.example.doasamagra.databinding.SurahPlayerFragmentBinding
import kotlinx.android.synthetic.main.surah_player_fragment.*

class SurahPlayerFragment : Fragment() {

    companion object {
        fun newInstance() = SurahPlayerFragment()
    }

    private lateinit var viewModel: SurahPlayerViewModel
    private lateinit var binding: SurahPlayerFragmentBinding
    private lateinit var surahItemAdapter: SurahItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.surah_player_fragment, container, false)
        surahItemAdapter = SurahItemAdapter()
        surahItemAdapter.data = listOf<Surah>()
        binding.surahList.adapter =  surahItemAdapter
        surahItemAdapter.setOnItemClickListener(object: SurahItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                viewModel.jcAudioList.value?.get(position)?.let { binding.jcplayer.playAudio(it) }
                binding.jcplayer.visibility = View.VISIBLE
                binding.jcplayer.createNotification()
            }
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SurahPlayerViewModel::class.java)
        viewModel.surahs.observe(viewLifecycleOwner, Observer {
            it?.let {
                surahItemAdapter.data = it
            }
        })
        viewModel.jcAudioList.observe(viewLifecycleOwner, Observer{
            it?.let {
                binding.jcplayer.initPlaylist(it, null)
            }
        })
        viewModel.loadData()
    }
}

