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

class RequestAdapter(
    private val requests: List<Participant>
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.requestItem_background_textView)
        val profileImageView: ShapeableImageView = itemView.findViewById(R.id.requestItem_profile_imageView)
        val approveButton: ImageButton = itemView.findViewById(R.id.requestItem_approve_imageButton)
        val deleteButton: ImageButton = itemView.findViewById(R.id.requestItem_delete_imageButton)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // start profile activity here
                    Log.d("ParticipantAdapter", "Clicked on request" + position)
                }
            }
            // Approve button listener
            approveButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // approve function
                    Log.d("ParticipantAdapter", "Approve request" + position)
                }
            }

            // Delete button listener
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // delete function
                    Log.d("ParticipantAdapter", "Delete request" + position)
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
        val participant = requests[position]
        holder.nameTextView.text = participant.name
        holder.profileImageView.setImageResource(participant.profileImageResId)
    }

    override fun getItemCount() = requests.size
}
