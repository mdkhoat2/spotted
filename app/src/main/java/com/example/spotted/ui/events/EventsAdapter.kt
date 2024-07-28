package com.example.spotted.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.classes.SportEvent

class EventsAdapter(private val events: List<SportEvent>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventIcon: ImageView = itemView.findViewById(R.id.event_icon)
        val eventTitle: TextView = itemView.findViewById(R.id.event_title)
        val eventLocation: TextView = itemView.findViewById(R.id.event_location)
        val eventTime: TextView = itemView.findViewById(R.id.event_time)
        val eventMore: ImageView = itemView.findViewById(R.id.event_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        // Set data for each item
        holder.eventTitle.text = event.title
        holder.eventLocation.text = event.location
        holder.eventTime.text = event.time
        // Set icon and other attributes as needed
        holder.eventIcon.setImageResource(event.iconResId)
        // holder.eventMore.setOnClickListener { /* handle more click */ }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
