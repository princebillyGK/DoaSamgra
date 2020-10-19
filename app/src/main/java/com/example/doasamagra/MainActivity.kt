package com.example.doasamagra

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.doasamagra.databinding.ActivityMainBinding
import com.example.doasamagra.util.OnSwipeTouchListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //AdMob Integration
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        //jcPlayer
        binding.jcPlayer.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                killJcPlayer()
            }

            override fun onSwipeRight() {
                killJcPlayer()
            }
        })
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        setUpNavigation()
    }

    private fun killJcPlayer() {
        binding.jcPlayer.kill()
        binding.jcPlayerHolder.visibility = View.GONE
    }

    private fun setUpNavigation() {
        NavigationUI.setupWithNavController(binding.bottomNavigation, navHostFragment.navController)
    }

}
