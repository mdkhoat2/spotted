package com.example.spotted.communication.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Message
import com.example.spotted.util.SupportUtil
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ChatAdapter(private val messageList: List<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (MessageAdapter(messageList[position]).isReceived()) VIEW_TYPE_RECEIVED else VIEW_TYPE_SENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sent_message, parent, false)
            MessageHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_received_message, parent, false)
            MessageHolder(view)
        }

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        val isTop = isTopMessage(position)
        val isBottom = isBottomMessage(position)

        when (holder.itemViewType) {
            VIEW_TYPE_SENT -> (holder as MessageHolder).bind(message, isTop, isBottom,position)
            VIEW_TYPE_RECEIVED -> (holder as MessageHolder).bind(message, isTop, isBottom,position)}
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    // ViewHolder
    inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageBody: TextView = itemView.findViewById(R.id.text_message_body)

        fun bind(message: Message, isTop: Boolean, isBottom: Boolean,position: Int) {
            messageBody.text = message.content
            setBubbleBackground(itemView, isTop, isBottom,!MessageAdapter(message).isReceived())

            if (shouldShowTimestamp(position)) {
                itemView.findViewById<TextView>(R.id.text_timestamp).text = SupportUtil.translateTime(message.sentAt)
            } else {
                itemView.findViewById<TextView>(R.id.text_timestamp).visibility = View.GONE
            }
        }
    }



    private fun setBubbleBackground(view: View, isTop: Boolean, isBottom: Boolean,isSent : Boolean = false) {

        val bubble = view.findViewById<View>(R.id.text_message_body)
        when {
            isTop && isBottom -> {
                bubble.setBackgroundResource(if (isSent) R.drawable.sent_message_4 else R.drawable.received_message_4)
            }
            isTop -> {
                bubble.setBackgroundResource(if (isSent) R.drawable.sent_message_1 else R.drawable.received_message_1)
            }
            isBottom -> {
                bubble.setBackgroundResource(if (isSent) R.drawable.sent_message_3 else R.drawable.received_message_3)
            }
            else -> {
                bubble.setBackgroundResource(if (isSent) R.drawable.sent_message_2 else R.drawable.received_message_2)
            }
        }
    }


    private fun formatTimestamp(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    private fun isTopMessage(position: Int): Boolean {
        // check if the last message was sent by the same person and if it was sent less than 1 hour ago
        if (position == 0) return true
        val currentMessage = messageList[position]
        val previousMessage = messageList[position - 1]
        if (currentMessage.sender != previousMessage.sender) return true

        return SupportUtil.getTimeBetween(currentMessage.sentAt, previousMessage.sentAt) > 3600000
    }

    private fun isBottomMessage(position: Int): Boolean {
        if (position == messageList.size - 1) return true
        val currentMessage = messageList[position]
        val nextMessage = messageList[position + 1]
        if (currentMessage.sender != nextMessage.sender) return true

        return SupportUtil.getTimeBetween(nextMessage.sentAt,currentMessage.sentAt ) > 3600000
    }

    private fun shouldShowTimestamp(position: Int): Boolean {
        if (position == 0) return true
        val currentMessage = messageList[position]
        val previousMessage = messageList[position - 1]

        val timeDifference = currentMessage.sentAt.time - previousMessage.sentAt.time

        return timeDifference > 3600000
    }

}