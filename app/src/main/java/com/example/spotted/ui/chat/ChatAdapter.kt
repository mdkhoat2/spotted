package com.example.spotted.ui.chat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Message
import com.example.spotted.communication.adapter.MessageAdapter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ChatAdapter(private val messageList: List<Message>,private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        val messageAdapter = MessageAdapter(messageList[position])
        return if (messageList[position].sender!=currentUserId) VIEW_TYPE_RECEIVED else VIEW_TYPE_SENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sent_message, parent, false)
            SentMessageHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_received_message, parent, false)
            ReceivedMessageHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]

        when (holder.itemViewType) {
            VIEW_TYPE_SENT -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

//    private fun getMessageBackground(position: Int, viewType: Int): Int {
//        return when {
//            // First message in a group (top)
//            position == 0 || messageList[position - 1].sender != messageList[position].sender -> {
//                if (viewType == VIEW_TYPE_SENT) R.drawable.message_top else R.drawable.message_top
//            }
//            // Last message in a group (bottom)
//            position == messageList.size - 1 || messageList[position + 1].sender != messageList[position].sender -> {
//                if (viewType == VIEW_TYPE_SENT) R.drawable.message_bottom else R.drawable.message_bottom
//            }
//            // Middle message in a group
//            else -> {
//                if (viewType == VIEW_TYPE_SENT) R.drawable.message_middle else R.drawable.message_middle
//            }
//        }
//    }

    // ViewHolder for sent messages
    inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageBody: TextView = itemView.findViewById(R.id.text_message_body)
       // private val sentAt: TextView = itemView.findViewById(R.id.text_message_time)

        fun bind(message: Message) {
            messageBody.text = message.content
            //sentAt.text = formatTimestamp(message.sentAt)
        }
    }

    // ViewHolder for received messages
    inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageBody: TextView = itemView.findViewById(R.id.text_message_body)
        //private val sentAt: TextView = itemView.findViewById(R.id.text_message_time)

        fun bind(message: Message) {
            messageBody.text = message.content
            //sentAt.text = formatTimestamp(message.sentAt)
        }
    }

    // Utility function to format timestamp
    private fun formatTimestamp(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }
}