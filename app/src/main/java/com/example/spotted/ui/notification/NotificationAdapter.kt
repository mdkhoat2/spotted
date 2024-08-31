import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotted.R

data class NotificationItem(val type: Int, val message: String)

class NotificationAdapter(
    private val context: Context,
    private val notificationList: List<NotificationItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_APPROVE_NEW = 1
        const val TYPE_REQUEST_ACCEPTED = 2
    }

    inner class ApproveNewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.notificationItem_approve_textView)

        fun bind(notification: NotificationItem) {
            messageTextView.text = notification.message
            itemView.setOnClickListener {

            }
        }
    }

    inner class RequestAcceptedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.notificationItem_accepted_textView)

        fun bind(notification: NotificationItem) {
            messageTextView.text = notification.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_APPROVE_NEW -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notification_approve_new, parent, false)
                ApproveNewViewHolder(view)
            }
            TYPE_REQUEST_ACCEPTED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notification_request_accepted, parent, false)
                RequestAcceptedViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return notificationList[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notification = notificationList[position]
        when (holder) {
            is ApproveNewViewHolder -> holder.bind(notification)
            is RequestAcceptedViewHolder -> holder.bind(notification)
        }
    }

    override fun getItemCount(): Int = notificationList.size
}
