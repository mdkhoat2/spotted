package com.example.spotted.ui.events

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Event

import com.example.spotted.databinding.FragmentEventsBinding
import com.example.spotted.ui.chat.ContactListActivity
import com.example.spotted.util.LayoutUtil
import java.sql.Timestamp

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val time =  Timestamp(System.currentTimeMillis());

    private val eventsList = listOf(
        Event("1", "Event 1", "Description 1", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
        Event("2", "Event 2", "Description 2", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
        Event("3", "Event 3", "Description 3", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
        Event("4", "Event 4", "Description 4", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
        Event("5", "Event 5", "Description 5", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
        Event("6", "Event 6", "Description 6", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
        Event("7", "Event 7", "Description 7", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
        Event("8", "Event 8", "Description 8", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
        Event("9", "Event 9", "Description 9", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
        Event("10", "Event 10", "Description 10", time, 60, listOf(0.0, 0.0), "type", "joinMode", 10, time),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val eventsViewModel =
            ViewModelProvider(this).get(EventsViewModel::class.java)

        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        LayoutUtil.setupUI(requireActivity(),root)

        setUpEvents(root)

        //button go to contact list activity
        val chatBtn = root.findViewById(R.id.btn_Chat) as Button
        chatBtn.setOnClickListener {
            val intent = Intent(requireActivity(), ContactListActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun setUpEvents(view: View)
    {
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_events)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val eventsAdapter = EventsAdapter(eventsList)
        recyclerView.adapter = eventsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}