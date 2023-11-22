package edu.put.neighborhoodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import edu.put.neighborhoodapp.adapter.ViewPagerAdapter
import edu.put.neighborhoodapp.fragments.AddFragment
import edu.put.neighborhoodapp.fragments.FragmentType
import edu.put.neighborhoodapp.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AddFragment.OnButtonClickedListener {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.navHostFragment)
        val navView: BottomNavigationView = findViewById(R.id.bottomNavView)
        navView.setupWithNavController(navController)

    }

    override fun onButtonClicked(fragmentTypes: MutableList<FragmentType>) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        navHostFragment?.view?.visibility = View.GONE // Hide NavHostFragment
        viewPager.visibility = View.VISIBLE // Show ViewPager
        val adapter = ViewPagerAdapter(this, fragmentTypes)
        viewPager.adapter = adapter

    }
}