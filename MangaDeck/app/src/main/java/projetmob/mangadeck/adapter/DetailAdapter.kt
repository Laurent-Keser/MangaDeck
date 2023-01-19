package projetmob.mangadeck.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import projetmob.mangadeck.databinding.ItemRatingBinding
import projetmob.mangadeck.model.ReviewProperty

/**
 * adapter pour le recyclerview du detail
 */
class DetailAdapter (query: Query, private val onClickListener: OnClickListener) :
    FirestoreAdapter<DetailAdapter.DetailPropertyViewHolder>(query) {

    class DetailPropertyViewHolder(private var binding: ItemRatingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot) : ReviewProperty? {
            val review: ReviewProperty? = snapshot.toObject(ReviewProperty::class.java)
            binding.review = review
            binding.executePendingBindings()
            return review
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ReviewProperty>() {
        override fun areItemsTheSame(oldItem: ReviewProperty, newItem: ReviewProperty): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ReviewProperty, newItem: ReviewProperty): Boolean {
            return oldItem.userId == newItem.userId && oldItem.mangaId == newItem.mangaId
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailPropertyViewHolder {
        return DetailPropertyViewHolder(ItemRatingBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: DetailPropertyViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            val property = holder.bind(snapshot)
            holder.itemView.setOnClickListener {
                onClickListener.onClick(property)
            }
        }
    }

    class OnClickListener(val clickListener: (reviewProperty: ReviewProperty?) -> Unit) {
        fun onClick(reviewProperty: ReviewProperty?) = clickListener(reviewProperty)
    }

}