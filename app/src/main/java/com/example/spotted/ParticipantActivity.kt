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
import com.example.spotted.databinding.ActivityParticipantBinding
import com.example.spotted.util.LayoutUtil

class ParticipantActivity : AppCompatActivity() {

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


        val participants = listOf(
            Participant("Nguyen Van A", R.drawable.profile),
            Participant("Nguyen Van B", R.drawable.profile),
            Participant("Nguyen Van C", R.drawable.profile),
            Participant("Nguyen Van D", R.drawable.profile),
            Participant("Nguyen Van E", R.drawable.profile),
            Participant("Nguyen Van F", R.drawable.profile),
            Participant("Nguyen Van G", R.drawable.profile),
            Participant("Nguyen Van H", R.drawable.profile),
            Participant("Nguyen Van I", R.drawable.profile),
            Participant("Nguyen Van J", R.drawable.profile)
        )

        val participant_recyclerView = findViewById<RecyclerView>(R.id.activityParticipant_participants_recyclerView)
        participant_recyclerView.layoutManager = LinearLayoutManager(this)
        participant_recyclerView.adapter = ParticipantAdapter(participants)

        val requests = listOf(
            Participant("Nguyen Van K", R.drawable.profile),
            Participant("Nguyen Van L", R.drawable.profile),
            Participant("Nguyen Van M", R.drawable.profile),
            Participant("Nguyen Van N", R.drawable.profile),
            Participant("Nguyen Van O", R.drawable.profile)
        )

        val request_recyclerView = findViewById<RecyclerView>(R.id.activityParticipant_request_recyclerView)
        request_recyclerView.layoutManager = LinearLayoutManager(this)
        request_recyclerView.adapter = RequestAdapter(requests)
    }

}