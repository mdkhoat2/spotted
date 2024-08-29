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
import com.example.spotted.classes.SportEvent
import com.example.spotted.databinding.FragmentEventsBinding
import com.example.spotted.ui.chat.ContactListActivity
import com.example.spotted.util.LayoutUtil

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val eventsList = listOf(
        SportEvent(R.drawable.ic_clock_black_24dp, "The Gatherer League", "Phu Tho Stadium", "17:15 / 16-05-2024"),
        SportEvent(R.drawable.ic_create_black_24dp, "ADL League for Physicians", "Thong Nhat Stadium", "17:15 / 17-05-2024"),
        // Add more events
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