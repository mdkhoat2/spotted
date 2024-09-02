package com.example.spotted.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Event
import com.example.spotted.util.SupportUtil

class EventsAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    //data class Event(
    //    // base on the backend data model
    //    val _id: String,
    //    val description: String,
    //    val start: Timestamp,
    //    val duration: Int,
    //    val location: List<Double>,
    //    val type: String,
    //    val joinMode: String,
    //    val maxParticipants: Int,
    //    val deadline: Timestamp
    //)

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventIcon: ImageView = itemView.findViewById(R.id.event_icon)
        val eventDescription: TextView = itemView.findViewById(R.id.event_description)
        val eventType: TextView = itemView.findViewById(R.id.event_type)
        val eventTime: TextView = itemView.findViewById(R.id.event_datetime)
        val moreBtn: ImageView = itemView.findViewById(R.id.event_more)
        val editBtn: ImageView = itemView.findViewById(R.id.event_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)

        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.eventDescription.text = event.description
        holder.eventType.text = event.type
        holder.eventTime.text = SupportUtil.translateTime(event.start)
        //holder.eventIcon.setImageResource(SupportUtil.getIcon(event.type))

        holder.moreBtn.setOnClickListener {

        }

        holder.editBtn.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
