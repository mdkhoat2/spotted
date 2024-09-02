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
    //some other time for testing
    val time2 = Timestamp(System.currentTimeMillis() + 100000000)
    val time3 = Timestamp(System.currentTimeMillis() + 2000000000)
    val time4 = Timestamp(System.currentTimeMillis() + 300000000)
    val time5 = Timestamp(System.currentTimeMillis() - 4000000000)

    private val eventsList = listOf(
        Event("1","Badminton 1",time,60, listOf(21.028511, 105.804817),"Sport","Everyone",10,time),
        Event("2","Badminton 2",time2,60, listOf(21.028511, 105.804817),"Sport","Everyone",10,time),
        Event("3","Badminton 3",time3,60, listOf(21.028511, 105.804817),"Sport","Everyone",10,time),
        Event("4","Badminton 4",time4,60, listOf(21.028511, 105.804817),"Sport","Everyone",10,time),
        Event("5","Badminton 5",time5,60, listOf(21.028511, 105.804817),"Sport","Everyone",10,time),
        Event("6","Badminton 6",time,60, listOf(21.028511, 105.804817),"Sport","Everyone",10,time),
        Event("7","Badminton 7",time,60, listOf(21.028511, 105.804817),"Sport","Everyone",10,time)
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