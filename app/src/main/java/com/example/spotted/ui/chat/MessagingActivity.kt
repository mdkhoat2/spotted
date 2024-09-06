package com.example.spotted.ui.chat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Message
import com.example.spotted.backend.dataModels.SendMessageRequest
import com.example.spotted.backend.dataServices.AuthDataService
import com.example.spotted.backend.dataServices.MessageDataService
import com.example.spotted.communication.adapters.ChatAdapter
import com.example.spotted.communication.live.MessageLive
import com.example.spotted.ui.profile.ProfileActivity
import com.example.spotted.util.LayoutUtil

class MessagingActivity() : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messageList: MutableList<Message>

    private var otherId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        otherId = intent.getStringExtra("otherId")?: ""

        messageList = mutableListOf()

        // List<Message>
        MessageDataService.getMessages(otherId,0) { messages ->
            if (messages != null) {
                println("message size "+messages.size)
                //flip the messages to display the latest message at the bottom

                val revMessages = messages.reversed()
                messageList.clear()
                messageList.addAll(revMessages)
                chatAdapter.notifyDataSetChanged()
                println("Messages: $messageList")
                recyclerView.scrollToPosition(messageList.size - 1)
            }   else
                Toast.makeText(this, "Failed to load messages", Toast.LENGTH_SHORT).show()
        }

        chatAdapter = ChatAdapter(messageList)
        recyclerView=findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter

        //when scoll up to limit, load more messages
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(-1)) {
                    val skip = messageList.size
                    MessageDataService.getMessages(otherId,skip) { messages ->
                        if (messages != null) {
                            val revMessages = messages.reversed()
                            messageList.addAll(0,revMessages)
                            chatAdapter.notifyItemRangeInserted(0,revMessages.size)
                            recyclerView.scrollToPosition(messages.reversed().size-1)
                        }
                    }
                }
            }
        })

        //display the messages from the bottom


        LayoutUtil.setupUI(this, recyclerView)

        val chatBox:EditText = findViewById(R.id.edit_text_message)
        val sendButton:Button = findViewById(R.id.button_send)

        sendButton.setOnClickListener {
            val message = SendMessageRequest(otherId, chatBox.text.toString())
            MessageDataService.sendMessage(message) { message ->
                if (message != null) {
                    messageList.add(message)
                    chatAdapter.notifyItemInserted(messageList.size - 1)

                    if (messageList.size > 1)
                        chatAdapter.notifyItemChanged(messageList.size - 2)

                    recyclerView.scrollToPosition(messageList.size - 1)
                    chatBox.text.clear()
                }
            }
        }

        //when click on the edit text, the keyboard will show up and Push the chat box up


        //
        val userName:TextView = findViewById(R.id.userName)
        AuthDataService.getUser(otherId) { user ->
            if (user != null) {
                userName.text = user.name
            }
        }

        val backButton: ImageView = findViewById(R.id.btn_back)
        backButton.setOnClickListener {
            finish()
        }

        setupShowProfile(otherId)

        MessageLive.setOnMessageReceivedCallback { message ->
            runOnUiThread {
                messageList.add(message)
                chatAdapter.notifyItemInserted(messageList.size - 1)
                recyclerView.smoothScrollToPosition(messageList.size - 1)

                if (messageList.size > 1)
                    chatAdapter.notifyItemChanged(messageList.size - 2)
            }
        }
    }

    private fun setupShowProfile(userID: String){
        val InfoButton:ImageView = findViewById(R.id.btn_info)
        InfoButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("otherId", userID)
            startActivity(intent)
        }
    }
}