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
    private var sortAsc:Boolean = true

    // EventAdapter is a class that extends RecyclerView.Adapter
    private lateinit var eventsAdapter: EventsAdapter

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
        val chatBtn: Button = root.findViewById(R.id.btn_Chat)
        chatBtn.setOnClickListener {
            val intent = Intent(requireActivity(), ContactListActivity::class.java)
            startActivity(intent)
        }

        val filterBtn: Button = root.findViewById(R.id.btn_filter)
        filterBtn.setOnClickListener {

        }

        val sortBtn: Button = root.findViewById(R.id.btn_sort)
        sortBtn.setOnClickListener {
            sortAsc = !sortAsc
            if (this.sortAsc) eventList.sortBy { it.first.start }
            else eventList.sortByDescending { it.first.start }

            eventsAdapter.notifyDataSetChanged()
        }

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
                eventsAdapter.notifyDataSetChanged()

                eventListCopy.clear()
                eventListCopy.addAll(eventList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}