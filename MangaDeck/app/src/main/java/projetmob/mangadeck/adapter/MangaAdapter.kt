package projetmob.mangadeck.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.get
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import projetmob.mangadeck.databinding.ItemMangaBinding
import projetmob.mangadeck.model.MangaProperty

/**
 * adapter pour le recyclerview de la liste manga
 */
class MangaAdapter(query: Query, private val onClickListener: OnClickListener) :
    FirestoreAdapter<MangaAdapter.MangaPropertyViewHolder>(query) {

    class MangaPropertyViewHolder(private var binding: ItemMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot): MangaProperty {
            val manga: MangaProperty? = snapshot.toObject(MangaProperty::class.java)
            manga!!.id = snapshot.id
            binding.property = manga
            binding.executePendingBindings()
            return manga
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MangaProperty>() {
        override fun areItemsTheSame(oldItem: MangaProperty, newItem: MangaProperty): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MangaProperty, newItem: MangaProperty): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MangaPropertyViewHolder {
        return MangaPropertyViewHolder(ItemMangaBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MangaPropertyViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            val property = holder.bind(snapshot)
            holder.itemView.setOnClickListener {
                onClickListener.onClick(property)
            }
            //style pour les deux couleurs
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#A3BFBF"))
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#B8D8D8"))
            }

            // Reset le viewHolder
            val layout = holder.itemView as RelativeLayout
            val img = layout[0] as ImageView
            img.setImageResource(0)
        }
    }

    class OnClickListener(val clickListener: (mangaProperty: MangaProperty?) -> Unit) {
        fun onClick(mangaProperty: MangaProperty?) = clickListener(mangaProperty)
    }
}