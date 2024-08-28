package com.example.spotted.ui.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.MessageDataService
import com.example.spotted.communication.adapters.MessageAdapter

class ContactListActivity : AppCompatActivity() {

    private lateinit var contactAdapter: ContactListWithChatAdapter
    private var contacts: MutableList<Contact> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_contacts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        contactAdapter = ContactListWithChatAdapter(contacts) { contact ->
            Toast.makeText(this, "Clicked on ${contact.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = contactAdapter

        println("Contact List Activity")
        println("size before notify: "+contacts.size)

        MessageDataService.getLastMessages { response ->
                    if (response!=null){
                        for (message in response){
                            println("Message: ")
                            println("Tin: "+ message.content )

                            val messageAdapter = MessageAdapter(message)
                            println("Status: "+ messageAdapter.isReceived() + " " + messageAdapter.getOtherUserId() + " " + messageAdapter.isRead())

                            AuthDataService.getUser(messageAdapter.getOtherUserId()) { user ->
                                if (user != null) {
                                    println(user.name)
                                    contacts+=(Contact(user._id, user.name, message.content,
                                        message.sentAt, null,messageAdapter.isRead()))

                                    runOnUiThread {
                                        contactAdapter.notifyDataSetChanged()
                                        println("size after notify: "+contacts.size)
                                    }
                                }
                            }

                        }

                    }
                }


    }
}
