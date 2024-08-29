package com.example.spotted.ui.chat
// an recycler view adapter for the contact list


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.util.SupportUtil
import java.sql.Timestamp

data class Contact(
    val id: String,
    val name: String,
    val lastMessage: String,
    val sentAt: Timestamp,
    val profileImageUrl: String?,
    val isRead: Boolean
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

            lastMessageTextView.text = contact.lastMessage+" . "+SupportUtil.getTimeSince(contact.sentAt.time)

            // Set click listener for the contact item
            itemView.setOnClickListener { onItemClick(contact) }

            if (!contact.isRead) {
                lastMessageTextView.setTextColor(Color.parseColor("#000000"))
                val contacts_read = itemView.findViewById(R.id.contact_read) as ImageView
                contacts_read.visibility = View.VISIBLE
            }
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
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}
