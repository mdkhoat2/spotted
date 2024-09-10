package com.example.spotted

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        // Apply insets listener to the BottomNavigationView to respect the navigation bar height
        ViewCompat.setOnApplyWindowInsetsListener(binding.navView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, systemBars.bottom) // Adjust bottom padding for navigation bar
            insets
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_event, R.id.navigation_create,
                R.id.navigation_notification, R.id.navigation_account
            )
        )

        navView.setupWithNavController(navController)

        val addButton: ImageButton = binding.mainActivityAddImageButton
        addButton.setOnClickListener {
            intent = Intent(this, CreateEventActivity::class.java)
            startActivity(intent)
        }

        NotificationLive.setOnUpdateBadgeCallback {
            val badge = navView.getOrCreateBadge(R.id.navigation_notification)
            badge.number = NotificationLive.getNumberUnreadNotification()
            badge.badgeTextColor = getColor(R.color.red_700)
            badge.isVisible = true
        }

        NotificationLive.statusOutOfNotificationFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationLive.statusOutOfMainActivity()
    }
}