package com.example.spotted

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.spotted.communication.live.NotificationLive
import com.example.spotted.databinding.ActivityMainBinding
import com.example.spotted.ui.create.CreateEventActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
                R.id.navigation_home,R.id.navigation_event, R.id.navigation_create, R.id.navigation_notification ,R.id.navigation_account
            )
        )

        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val addButton : ImageButton = findViewById(R.id.mainActivity_add_imageButton)
        addButton.setOnClickListener {
            intent = Intent(this, CreateEventActivity::class.java)
            startActivity(intent)
        }

        if(!NotificationLive.isHasUpdateBadgeCallback()){
            NotificationLive.setOnUpdateBadgeCallback {
                val badge = navView.getOrCreateBadge(R.id.navigation_notification)
                badge.number = NotificationLive.getNumberUnreadNotification()
                badge.badgeTextColor = getColor(R.color.red_700)
                badge.isVisible = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationLive.statusOutOfMainActivity()
    }
}