package com.example.spotted.ui.notification

import NotificationAdapter
import NotificationItem
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.NotificationModel
import com.example.spotted.databinding.FragmentNotificationBinding
import com.example.spotted.ui.event.MapActivity
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.SupportUtil

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?
    ): View {
        val createEventViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentNotificationRecyclerView.layoutManager = layoutManager

        val adapter = NotificationAdapter(requireContext(), createNotificationItems(getNotifications()))
        binding.fragmentNotificationRecyclerView.adapter = adapter

        val header : TextView = binding.fragmentNotificationHeader
        LayoutUtil.applyVariableFont(this, header, "'wght' 500, 'wdth' 150")

        return root
    }

    private fun getNotifications(): List<NotificationModel>{
        val notifications = mutableListOf<NotificationModel>()
        for (i in 1..10){
            notifications.add(NotificationModel("New Request", "A new request to join PR league is waiting for your approval."))
        }
        return notifications
    }

    private fun createNotificationItems(listNotifications: List<NotificationModel>): List<NotificationItem>{
        val notifications = mutableListOf<NotificationItem>()
        for (notification in listNotifications){
            notifications.add(SupportUtil.getNotificationItem(notification)!!)
        }
        return notifications
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}