package com.example.spotted

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.spotted.databinding.ActivityMainBinding
import com.example.spotted.backend.AuthDataService
import com.example.spotted.util.LayoutUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var authDataService = AuthDataService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_create, R.id.navigation_notifications,R.id.navigation_account
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        authDataService.login("caynhat05062004@gmail.com", "123456") { response ->
            if (response != null) {
                println("Token: ${response.token}")
                println("User: ${response.user}")
            } else {
                println("Login failed")
            }
        }
        println(BuildConfig.API_BASE_URL)
    }
}