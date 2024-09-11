package com.example.spotted.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spotted.databinding.FragmentAccountBinding
import com.example.spotted.util.LayoutUtil

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fragmentAccountAppCompatButtonLogOut.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.fragmentAccountAppCompatButtonChangePassword.setOnClickListener{
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.fragmentAccountAppCompatButtonEditProfile.setOnClickListener{
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        val header : TextView = binding.fragmentAccountTextViewHeader
        LayoutUtil.applyVariableFont(this, header, "'wght' 500, 'wdth' 150")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}