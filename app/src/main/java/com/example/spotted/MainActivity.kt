package com.example.spotted

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.spotted.databinding.ActivityMainBinding
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.backend.dataServices.MessageDataService

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
                R.id.navigation_home, R.id.navigation_create, R.id.navigation_event,R.id.navigation_account
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        AuthDataService.forgotPassword("caynhat05062004@gmail.com") {
//            println(DataService.getMsg())
//        };
        println("con cs")
        AuthDataService.login("caynhat05062004@gmail.com", "123456") { response ->
            if (response != null) {
                println("Token: ${response.token}")
                println("User: ${response.user}")
//                MessageDataService.getLastMessages { response ->
//                    if (response!=null){
//                    }
//                }
//                MessageDataService.getMessages("66a39662a7e21e8958e46f3e"){
//                    if (response!=null){
//                        println("halaluyah")
//                    }
////                    }
//                }

//                AuthDataService.resetPassword("g03hjlw9", "123456") {
//                    response -> println(DataService.getMsg())
//                }

//                Uncomment to test sending message.
//                This action will add data to database, check database to verify
//                and do not spam
//                MessageDataService.sendMessage("66a39662a7e21e8958e46f3e", "Hello") { message ->
//                    if (message != null) {
//                        println("Message: ${message}")
//                    } else {
//                        println(DataService.getMsg())
//                    }
//                }
            } else {
                println("scscs ")
                println(DataService.getMsg())
            }
        }
    }
}