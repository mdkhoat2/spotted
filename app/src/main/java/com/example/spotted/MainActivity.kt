package com.example.spotted

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.spotted.databinding.ActivityMainBinding
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.backend.dataServices.MessageDataService
import com.example.spotted.testing.Testing
import com.example.spotted.ui.create.CreateEventActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val navigateTo = intent.getStringExtra("navigateTo")
        if (navigateTo == "home") {
            navController.navigate(R.id.navigation_home)
        }

        navView.setupWithNavController(navController)



        //Testing.test()
    }
}