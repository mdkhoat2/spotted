package com.example.spotted.backend.dataModels
// an recycler view adapter for the contact list


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataServices.DataService
import com.example.spotted.util.SupportUtil
import java.sql.Timestamp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

data class Contact(
    val id: String,
    val name: String,
    var lastMessage: String,
    var sentAt: Timestamp,
    var profileImageUrl: String?,
    var isRead: Boolean
)

class ContactListWithChatAdapter(
    private val contacts: List<Contact>,
    private val onItemClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactListWithChatAdapter.ContactViewHolder>() {

    // ViewHolder class
    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.contact_name)
        val lastMessageTextView: TextView = view.findViewById(R.id.contact_last_message)
        val profileImageView: ImageView = view.findViewById(R.id.contact_profile_image)

        fun bind(contact: Contact, onItemClick: (Contact) -> Unit) {
            nameTextView.text = contact.name

            lastMessageTextView.text = buildString {
                append(contact.lastMessage)
                append(" . ")
                append(SupportUtil.getTimeSince(contact.sentAt.time))
            }
            if (DataService.getAuthProfile()?._id ?: ""==contact.id){
                lastMessageTextView.text = buildString {
                    append("You: ")
                    append(contact.lastMessage)
                    append(" . ")
                    append(SupportUtil.getTimeSince(contact.sentAt.time))
                }
            }


            itemView.setOnClickListener { onItemClick(contact) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        // Inflate the layout for each item in the list
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        // Bind the data to the views
        val contact = contacts[position]
        holder.bind(contact, onItemClick)
        if (contact.profileImageUrl != null) {
            Glide.with(holder.profileImageView.context)
                .load(contact.profileImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.profileImageView)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}
