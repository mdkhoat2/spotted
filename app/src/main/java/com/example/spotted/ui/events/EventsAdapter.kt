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
    //    val _id: String,
    //    val title: String,
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
        val eventTitle: TextView = itemView.findViewById(R.id.event_title)
        val eventTime: TextView = itemView.findViewById(R.id.event_time)
        val eventDate: TextView = itemView.findViewById(R.id.event_date)
        val eventDetail: ImageView = itemView.findViewById(R.id.event_detail)
        val eventMore: ImageView = itemView.findViewById(R.id.event_more)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventTitle.text = event.title
        holder.eventTime.text = SupportUtil.translateTime(event.start)
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
