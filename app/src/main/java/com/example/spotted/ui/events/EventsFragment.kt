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

    private var eventList = mutableListOf<Event>()
    private var adminId = mutableListOf<String>()

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
        val eventsAdapter = EventsAdapter(eventList, adminId)
        recyclerView.adapter = eventsAdapter

        eventList.clear()


//        EventDataService.getJoinedEvents { joinedEvents ->
//            if (joinedEvents != null) {
//                eventList.clear()
//                eventList.addAll(joinedEvents.map { it.event })
//
//                adminId.clear()
//                //java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String com.example.spotted.backend.dataModels.Admin.getUserID()' on a null object reference
//                adminId.addAll(joinedEvents.map { it.admin.userID })
//                eventsAdapter.notifyDataSetChanged()
//            }
//        }
        //data class Admin(
        //    val _id: String,
        //    val eventID: String,
        //    val userID: String,
        //    val mode: String,
        //    val createdAt: Timestamp,
        //    val __v: Int
        //)
        //data class JoinedEvent(
        //    val joinedAt: Timestamp,
        //    val event: Event,
        //    val admin: Admin
        //)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}