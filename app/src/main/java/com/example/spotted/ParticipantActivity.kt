package com.example.spotted

import RequestAdapter
import RequestListener
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

class ParticipantActivity : AppCompatActivity(), RequestListener {

    private lateinit var participants: MutableList<Participant>
    private lateinit var requests: MutableList<Pair<Participant,String>>

    private lateinit var binding: ActivityParticipantBinding

    lateinit var participantAdapter: ParticipantAdapter
    lateinit var requestAdapter: RequestAdapter

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

        participantAdapter = ParticipantAdapter(participants)
        requestAdapter = RequestAdapter(requests, this)

        val participant_recyclerView = findViewById<RecyclerView>(R.id.activityParticipant_participants_recyclerView)
        participant_recyclerView.layoutManager = LinearLayoutManager(this)

        val request_recyclerView = findViewById<RecyclerView>(R.id.activityParticipant_request_recyclerView)
        request_recyclerView.layoutManager = LinearLayoutManager(this)


        EventDataService.getParticipants(EventDataService.getCurrentEvent()!!._id) {
            if (it != null) {
                for (participant in it) {
                    participants.add(Participant(participant, R.drawable.profile))
                }

                participant_recyclerView.adapter = participantAdapter
            }
        }

        EventDataService.getRequests(EventDataService.getCurrentEvent()!!._id) {
            if (it != null) {
                for (request in it) {
                    requests.add(Participant(request.users[0], R.drawable.profile) to request.request._id)
                }

                request_recyclerView.adapter = requestAdapter
            }
        }
    }

    // listener for the addParticipant function in RequestAdapter

    override fun onRequestApproved(position : Int) {
        val participant = requests[position].first
        participants.add(participant)
        participantAdapter.notifyItemInserted(participants.size - 1)
        requests.removeAt(position)
        requestAdapter.notifyItemRemoved(position)
    }

    override fun onRequestRejected(position : Int) {
        requests.removeAt(position)
        requestAdapter.notifyItemRemoved(position)
    }


}