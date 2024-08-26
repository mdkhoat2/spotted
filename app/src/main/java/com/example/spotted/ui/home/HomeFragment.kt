package com.example.spotted.ui.home

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.spotted.R
import com.example.spotted.databinding.FragmentHomeBinding
import com.example.spotted.ui.event.MapActivity
import com.example.spotted.util.LayoutUtil

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        LayoutUtil.setupUI(requireActivity(),root)

        val MapBtn: AppCompatButton = root.findViewById(R.id.fragmentHome_button_openMap)
        MapBtn.setOnClickListener{
            val intent = Intent(activity,MapActivity::class.java)
            startActivity(intent)
        }

        val header: TextView = binding.fragmentHomeHeader

        val createEventBtn: AppCompatButton = root.findViewById(R.id.fragmentHome_button_createEvent)

        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,createEventBtn,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,MapBtn,"'wght' 500, 'wdth' 150")

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}