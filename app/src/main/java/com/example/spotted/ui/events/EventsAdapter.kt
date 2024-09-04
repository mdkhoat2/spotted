package com.example.spotted.ui.events

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Event
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.ui.event.EventDetailActivity
import com.example.spotted.util.SupportUtil

class EventsAdapter(private val events: List<Pair<Event,String>>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

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
        val event = events[position].first

        holder.eventDescription.text = event.description
        holder.eventType.text = event.type
        holder.eventTime.text = SupportUtil.translateTime(event.start)
        //holder.eventIcon.setImageResource(SupportUtil.getIcon(event.type))

        // set on click listener for the whole item
        holder.itemView.setOnClickListener {
            val intent: Intent = Intent(holder.itemView.context, EventDetailActivity::class.java)
            intent.putExtra("id", event._id)
            intent.putExtra("description", event.description)
            intent.putExtra("type", event.type)
            intent.putExtra("start", holder.eventTime.text)
            intent.putExtra("latitude", event.latitude)
            intent.putExtra("longitude", event.longitude)
            intent.putExtra("address", event.address)

            if (events[position].second  == DataService.getAuthProfile()?._id) {
                intent.putExtra("permission", "manager")
            } else {
                intent.putExtra("permission", "guest")
            }

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
