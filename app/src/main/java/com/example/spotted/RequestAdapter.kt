import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import android.widget.ImageButton
import android.widget.TextView
import com.example.spotted.Participant
import com.example.spotted.R
import com.example.spotted.backend.dataModels.Event
import com.example.spotted.backend.dataServices.EventDataService
import com.example.spotted.ui.profile.ProfileActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class RequestAdapter(
    private var requests: MutableList<Pair<Participant,String>>,
    private val listener: RequestListener
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.requestItem_background_textView)
        val profileImageView: ShapeableImageView = itemView.findViewById(R.id.requestItem_profile_imageView)
        private val approveButton: ImageButton = itemView.findViewById(R.id.requestItem_approve_imageButton)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.requestItem_delete_imageButton)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(itemView.context, ProfileActivity::class.java)
                    intent.putExtra("otherId", requests[position].first.user._id)
                    intent.putExtra("isNeedSent", true)
                    itemView.context.startActivity(intent)
                }
            }

            // Approve button listener

            approveButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    EventDataService.responseToRequest(requests[position].second, "accept") {
                        if (it != null) {
                            listener.onRequestApproved(position)
                        }
                    }
                }
            }

            // Delete button listener
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    EventDataService.responseToRequest(requests[position].second, "reject") {
                        if (it != null) {
                            listener.onRequestRejected(position)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false) // Ensure you update with the correct item layout name
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val participant = requests[position].first
        holder.nameTextView.text = participant.user.name
        if (participant.user.avatarUrl != null) {
            Glide.with(holder.profileImageView.context)
                .load(participant.user.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.profileImageView)
        }
    }

    override fun getItemCount() = requests.size
}
interface RequestListener {
    fun onRequestApproved(position : Int)
    fun onRequestRejected(position : Int)
}
