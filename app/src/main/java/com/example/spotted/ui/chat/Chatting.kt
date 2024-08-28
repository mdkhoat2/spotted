package com.example.spotted.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Message
import java.time.Instant
import java.time.format.DateTimeFormatter

class Chatting : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messageList: MutableList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val currentUserId = "user456" // Replace with the actual current user ID

        println("sv 1")

        messageList = mutableListOf(
            Message("user123", "user456", "Hello!", null, null),
            Message("user456", "user123", "Hi there!", null, null),
            Message("user456", "user123", "I am Dead", null, null),
            Message("user456", "user123", "Thank you", null, null),
            Message("user123", "user456", "How are you?", null, null),
            Message("user456", "user123", "I'm good, thanks!", null, null)
        )

        println("sv 2")

        chatAdapter = ChatAdapter(messageList, currentUserId)
        println("sv 3")
        recyclerView=findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        println("sv 4")
        recyclerView.adapter = chatAdapter
    }
}