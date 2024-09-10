package com.example.spotted.ui.events

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Event
import com.example.spotted.backend.dataServices.EventDataService

import com.example.spotted.databinding.FragmentEventsBinding
import com.example.spotted.ui.chat.ContactListActivity
import com.example.spotted.util.LayoutUtil
import java.sql.Timestamp

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var eventList = mutableListOf<Pair<Event,String>>()
    private var eventListCopy = mutableListOf<Pair<Event,String>>()
    private var sortUsing: Int = 0

    // EventAdapter is a class that extends RecyclerView.Adapter
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val eventsViewModel =
            ViewModelProvider(this)[EventsViewModel::class.java]

        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        LayoutUtil.setupUI(requireActivity(),root)

        setUpEvents(root)

        //spinner filter
        val sports = resources.getStringArray(R.array.sports)
        val sportAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,sports)
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerSport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sport = sports[position]
                println("Filter: "+sport)
                if (sport == "All") eventsAdapter.filter.filter("")

                else eventsAdapter.filter.filter(sport)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spinnerSport.adapter=sportAdapter


        //button sort
        val sortBtn: ImageButton = root.findViewById(R.id.btn_sort)
        sortBtn.setOnClickListener {
            when (sortUsing) {
                0 -> {
                   eventsAdapter.sortBy(SortType.NAME)
                }
                1 -> {
                    eventsAdapter.sortBy(SortType.TYPE)
                }
                2 -> {
                    eventsAdapter.sortBy(SortType.DATE)
                }

            }
            sortUsing = (sortUsing+1)%3
        }

        val header : TextView = root.findViewById(R.id.fragmentEvents_header)
        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")


        return root
    }

    private fun setUpEvents(view: View)
    {
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_events)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        eventsAdapter = EventsAdapter(eventList)
        recyclerView.adapter = eventsAdapter

        EventDataService.getJoinedEvents { joinedEvents ->
            if (joinedEvents != null) {

                eventList.clear()

                // admin can be null so we need to check
                for (a in joinedEvents) {
                    if (a.admin != null) {
                        val b:Pair<Event,String> = Pair(a.event,a.admin._id)
                        eventList.add(b)
                    } else {
                        val b:Pair<Event,String> = Pair(a.event,"")
                        eventList.add(b)
                    }
                }

                eventListCopy.clear()
                eventListCopy.addAll(eventList)

                eventsAdapter.filter.filter("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}