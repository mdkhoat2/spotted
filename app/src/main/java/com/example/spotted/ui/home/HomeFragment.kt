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
import com.example.spotted.backend.dataModels.Event
import com.example.spotted.backend.dataModels.JoinedEvent
import com.example.spotted.backend.dataServices.EventDataService
import com.example.spotted.databinding.FragmentHomeBinding
import com.example.spotted.ui.chat.ContactListActivity
import com.example.spotted.ui.chat.MessagingActivity
import com.example.spotted.ui.event.EventDetailActivity
import com.example.spotted.ui.event.MapActivity
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.SupportUtil
import java.security.Permission

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

        val createEventBtn: AppCompatButton = root.findViewById(R.id.fragmentHome_button_messages)

        val upcoming: TextView = binding.fragmentHomeTextViewUpcoming

        val upcomingcard: View = root.findViewById(R.id.fragmentHome_background_upcoming)

        val title: TextView = binding.fragmentHomeTextViewUpcomingEventTitle

        val category: TextView = binding.fragmentHomeTextViewUpcomingEventCategory

        val location: TextView = binding.fragmentHomeTextViewUpcomingEventLocation

        val time: TextView = binding.fragmentHomeTextViewUpcomingEventTime

        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,createEventBtn,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,MapBtn,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,upcoming,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,title,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,category,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,location,"'wght' 500, 'wdth' 150")
        LayoutUtil.applyVariableFont(this,time,"'wght' 500, 'wdth' 150")

        val messageBtn: Button = root.findViewById(R.id.fragmentHome_button_messages)
        messageBtn.setOnClickListener{
            val intent = Intent(activity,ContactListActivity::class.java)
            startActivity(intent)
        }

        EventDataService.getJoinedEvents { joinedEvents ->
            if (joinedEvents != null) {

                var nearestEvent: JoinedEvent? = null
                for (a in joinedEvents) {
                    if (nearestEvent == null) {
                        nearestEvent = a
                    } else {
                        if (a.event.start < nearestEvent.event.start) {
                            nearestEvent = a
                        }
                    }
                }

                if (nearestEvent != null) {
                    title.text = nearestEvent.event.title
                    category.text = nearestEvent.event.type
                    location.text = nearestEvent.event.address
                    time.text = SupportUtil.translateTime(nearestEvent.event.start)

                    EventDataService.setCurrentEvent(nearestEvent.event)

                    var Permission = "none"

                    EventDataService.getRole(nearestEvent.event._id) {
                        if (it != null) Permission = it
                    }

                    upcomingcard.setOnClickListener(View.OnClickListener {
                        val intent = Intent(activity, EventDetailActivity::class.java)
                        intent.putExtra("permission", Permission)
                        startActivity(intent)
                    })
                }
            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}