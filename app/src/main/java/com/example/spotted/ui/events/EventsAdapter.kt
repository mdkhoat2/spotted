package com.example.spotted.ui.events

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Event
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.backend.dataServices.EventDataService
import com.example.spotted.ui.event.EventDetailActivity
import com.example.spotted.util.SupportUtil

class EventsAdapter(private val events: List<Pair<Event,String>>) :
    RecyclerView.Adapter<EventsAdapter.EventViewHolder>(), Filterable {

    private var filteredEvents: List<Pair<Event, String>> = events

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventIcon: ImageView = itemView.findViewById(R.id.event_icon)
        val eventName: TextView = itemView.findViewById(R.id.event_name)
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
        val event = filteredEvents[position].first

        holder.eventName.text = event.title
        holder.eventType.text = event.type
        holder.eventTime.text = SupportUtil.translateTime(event.start)
        holder.eventIcon.setImageResource(SupportUtil.getSportIcon(event.type))

        if (events[position].second  == "") {
            holder.editBtn.visibility = View.GONE
        } else {
            holder.editBtn.visibility = View.VISIBLE
        }

        // set on click listener for the whole item
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventDetailActivity::class.java)

            EventDataService.getRole(event._id) {
                if (it != null) {
                    intent.putExtra("permission", it)
                } else {
                    intent.putExtra("permission", "none")
                }

                EventDataService.setCurrentEvent(event)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredEvents.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterString = constraint?.toString() ?: ""
                filteredEvents = if (filterString.isEmpty()) {
                    events
                } else {
                    events.filter {
                        it.first.type.contains(filterString, ignoreCase = true)
                    }
                }
                return FilterResults().apply { values = filteredEvents }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredEvents = results?.values as List<Pair<Event, String>>
                notifyDataSetChanged()
            }
        }
    }

    fun sortBy(sortType: SortType) {
        filteredEvents = when (sortType) {
            SortType.NAME -> filteredEvents.sortedBy { it.first.title }
            SortType.TYPE -> filteredEvents.sortedBy { it.first.type }
            SortType.DATE -> filteredEvents.sortedBy { it.first.start }
        }
        notifyDataSetChanged()
    }
}

enum class SortType {
    NAME,
    TYPE,
    DATE
}
