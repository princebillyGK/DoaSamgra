package com.example.doasamagra

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        setUpNavigation()
    }

    private fun setUpNavigation() {
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
    }

//    private fun setBottomNavigationListeners() {
//        bottomNavigationView.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.qiblaCompass-> {
//                    supportActionBar?.title = getString(R.string.qbla_menu_title)
//                    navController.navigate(R.id.qiblaCompassFragment)
//                    true
//                }
//                R.id.hadis-> {
//                    supportActionBar?.title = getString(R.string.hadis_menu_title)
//                    navController.navigate(R.id.hadisFragment)
//                    true
//                }
//                R.id.doa-> {
//                    supportActionBar?.title = getString(R.string.doa_menu_title)
//                    navController.navigate(R.id.doaFragment)
//                    true
//                }
//                R.id.surahPlayer-> {
//                    supportActionBar?.title = getString(R.string.surah_menu_title)
//                    navController.navigate(R.id.surahPlayerFragment)
//                    true
//                }
//                else -> false
//            }
//        }
//    }

}
