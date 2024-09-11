package com.example.spotted
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import android.widget.TextView
import com.example.spotted.ui.profile.ProfileActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ParticipantAdapter(
    private val participants: MutableList<Participant>
) : RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>() {

    inner class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.participantItem_background_textView)
        val profileImageView: ShapeableImageView = itemView.findViewById(R.id.participantItem_profile_imageView)
        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(itemView.context, ProfileActivity::class.java)
                    intent.putExtra("otherId", participants[position].user._id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_participant, parent, false)

        return ParticipantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val participant = participants[position]
        holder.nameTextView.text = participant.user.name
        if (participant.user.avatarUrl != null) {
            Glide.with(holder.profileImageView.context)
                .load(participant.user.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.profileImageView)
        }
    }

    override fun getItemCount() = participants.size
}
