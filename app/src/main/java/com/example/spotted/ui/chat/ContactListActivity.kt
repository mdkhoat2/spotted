package com.example.spotted.ui.chat

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
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
import com.example.spotted.databinding.ActivityContactListBinding
import com.example.spotted.util.LayoutUtil
import com.example.spotted.util.SupportUtil

class ContactListActivity : AppCompatActivity() {

    private lateinit var contactAdapter: ContactListWithChatAdapter
    private var contacts: MutableList<Contact> = mutableListOf()
    private lateinit var binding: ActivityContactListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.activityContactListImageButtonBack.setOnClickListener {
            finish()
        }

        val dialog = SupportUtil.createProgressDialog(this)

        val header : TextView = findViewById(R.id.activityContactList_textView_header)
        LayoutUtil.applyVariableFont(this,header,"'wght' 500, 'wdth' 150")

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_contacts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        contactAdapter = ContactListWithChatAdapter(contacts) { contact ->
            contact.isRead = true
            val intent = Intent(this, MessagingActivity::class.java)
            intent.putExtra("otherId", contact.id)
            startActivity(intent)
        }

        recyclerView.adapter = contactAdapter

        MessageDataService.getLastMessages { response ->
                    if (response!=null){
                        response.sortedBy { it.sentAt }

                        for (message in response){

                            val messageAdapter = MessageAdapter(message)
                            println("Status: "+ message.sentAt + " " + messageAdapter.getOtherUserId() + " " + messageAdapter.isRead())

                            AuthDataService.getUser(messageAdapter.getOtherUserId()) { user ->
                                if (user != null) {
                                    contacts+=(Contact(user._id, user.name, message.content,
                                        message.sentAt, user.avatarUrl,messageAdapter.isRead()))

                                    runOnUiThread {
                                        contactAdapter.notifyItemInserted(contacts.size - 1)
                                    }
                                }
                            }
                        }
                    }
                    dialog.dismiss()
                }

        MessageLive.setOnMessageReceivedCallback { message ->
            runOnUiThread {
                var contact = contacts.find { it.id == message.sender || it.id == message.receiver }
                val position = contacts.indexOf(contact)

                if (contact != null) {
                    contact.lastMessage = message.content
                    contact.sentAt= message.sentAt
                    contact.isRead = false
                    contacts.removeAt(position)
                    contacts.add(0, contact)
                    contactAdapter.notifyItemMoved(position, 0)
                } else {
                    AuthDataService.getUser(message.sender) { user ->
                        if (user != null) {
                            contacts.add(0,Contact(user._id, user.name, message.content, message.sentAt, null, false))
                            contactAdapter.notifyItemInserted(0)
                        }
                    }
                }

                contactAdapter.notifyItemChanged(0)
            }
        }
    }
}
