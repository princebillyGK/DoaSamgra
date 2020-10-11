package com.example.doasamagra.surahplayer

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.doasamagra.R

class SurahPlayerFragment : Fragment() {

    companion object {
        fun newInstance() = SurahPlayerFragment()
    }

    private lateinit var viewModel: SurahPlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.surah_player_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SurahPlayerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}