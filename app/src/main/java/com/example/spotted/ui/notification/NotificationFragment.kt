package com.example.spotted.ui.notification

import NotificationAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotted.backend.dataModels.NotificationItem
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.backend.dataServices.NotificationDataService
import com.example.spotted.databinding.FragmentNotificationBinding
import com.example.spotted.ui.account.Helper
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.SupportUtil
import java.time.Instant

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: NotificationAdapter

    private val notificationListItem = mutableListOf<NotificationItem>()

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentNotificationRecyclerView.layoutManager = layoutManager

        val progressDialog = SupportUtil.createProgressDialog(requireContext())

        NotificationDataService.getNotifications(Instant.now().toString()){
            if (it != null){
                println(Instant.now().toString() + " " + DataService.authToken)
                notificationListItem.clear()
                for (item in it){
                    item.typeInt = SupportUtil.getNotificationType(item.type)
                    notificationListItem.add(item)
                }
                adapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }else{
                progressDialog.dismiss()
            }
        }

        adapter = NotificationAdapter(requireContext(),notificationListItem)
        binding.fragmentNotificationRecyclerView.adapter = adapter

        val header : TextView = binding.fragmentNotificationHeader
        LayoutUtil.applyVariableFont(this, header, "'wght' 500, 'wdth' 150")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}