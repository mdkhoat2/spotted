package com.example.spotted.ui.chat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Contact
import com.example.spotted.backend.dataModels.ContactListWithChatAdapter
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.MessageDataService
import com.example.spotted.communication.adapters.MessageAdapter
import com.example.spotted.communication.live.MessageLive

class ContactListActivity : AppCompatActivity() {

    private lateinit var contactAdapter: ContactListWithChatAdapter
    private var contacts: MutableList<Contact> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_contacts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        contactAdapter = ContactListWithChatAdapter(contacts) { contact ->
            val intent = Intent(this, MessagingActivity::class.java)
            intent.putExtra("otherId", contact.id)
            startActivity(intent)
        }

        recyclerView.adapter = contactAdapter

        MessageDataService.getLastMessages { response ->
                    if (response!=null){
                        for (message in response){

                            val messageAdapter = MessageAdapter(message)
                            println("Status: "+ messageAdapter.isReceived() + " " + messageAdapter.getOtherUserId() + " " + messageAdapter.isRead())

                            AuthDataService.getUser(messageAdapter.getOtherUserId()) { user ->
                                if (user != null) {
                                    contacts+=(Contact(user._id, user.name, message.content,
                                        message.sentAt, null,messageAdapter.isRead()))

                                    runOnUiThread {
                                        contactAdapter.notifyItemInserted(contacts.size - 1)
                                    }
                                }
                            }

                        }

                    }
                }

        MessageLive.setOnMessageReceivedCallback { message ->
            runOnUiThread {
                // Check if the contact is already in the list find position and update

                var contact = contacts.find { it.id == message.sender || it.id == message.receiver }
                val position = contacts.indexOf(contact)

                if (contact != null) {
                    contact.lastMessage = message.content
                    contact.sentAt = message.sentAt
                    contactAdapter.notifyItemChanged(position)
                } else {
                    AuthDataService.getUser(message.sender) { user ->
                        if (user != null) {
                            contacts.add(Contact(user._id, user.name, message.content, message.sentAt, null, false))
                            contactAdapter.notifyItemInserted(contacts.size - 1)
                        }
                    }
                }
            }
        }
    }
}
