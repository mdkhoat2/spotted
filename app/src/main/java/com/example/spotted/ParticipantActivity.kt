package com.example.spotted

import RequestAdapter
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.backend.dataModels.User
import com.example.spotted.backend.dataServices.EventDataService
import com.example.spotted.databinding.ActivityParticipantBinding
import com.example.spotted.util.LayoutUtil

class ParticipantActivity : AppCompatActivity() {

    private lateinit var participants: MutableList<Participant>
    private lateinit var requests: MutableList<Pair<Participant,String>>

    private lateinit var binding: ActivityParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val back : ImageButton = binding.activityParticipantImageButtonBack
        back.setOnClickListener {
            finish()
        }
        val header : TextView = binding.activityParticipantTextViewHeader
        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")


        participants = mutableListOf()
        requests = mutableListOf()

        val participant_recyclerView = findViewById<RecyclerView>(R.id.activityParticipant_participants_recyclerView)
        participant_recyclerView.layoutManager = LinearLayoutManager(this)

        val request_recyclerView = findViewById<RecyclerView>(R.id.activityParticipant_request_recyclerView)
        request_recyclerView.layoutManager = LinearLayoutManager(this)


        EventDataService.getParticipants(EventDataService.getCurrentEvent()!!._id) {
            if (it != null) {
                participants = it.map { user -> Participant(user, R.drawable.profile)}.toMutableList()
                participant_recyclerView.adapter = ParticipantAdapter(participants)
            }
        }

        EventDataService.getRequests(EventDataService.getCurrentEvent()!!._id) {
            if (it != null) {
                for (request in it) {
                    requests.add(Participant(request.users[0], R.drawable.profile) to request.request._id)
                }

                request_recyclerView.adapter = RequestAdapter(requests)
                println(it)
            }
        }
    }

}