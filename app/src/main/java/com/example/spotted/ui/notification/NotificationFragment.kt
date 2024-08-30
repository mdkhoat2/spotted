package com.example.spotted.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.spotted.databinding.FragmentCreateEventBinding
import com.example.spotted.ui.event.MapActivity

class NotificationFragment : Fragment() {

    private var _binding: FragmentCreateEventBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?
    ): View {
        val createEventViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        _binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val intent = Intent(activity, MapActivity::class.java)
//        startActivity(intent)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}