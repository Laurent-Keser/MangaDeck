package projetmob.mangadeck.binding

import android.content.res.Resources
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import projetmob.mangadeck.R
import projetmob.mangadeck.model.MangaProperty
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * affiche l'image des mangas
 */
@BindingAdapter("url")
fun bindImageManga(view: ImageView, url: String) {
    val imageRef = Firebase.storage.reference.child("mangas").child(url)
    imageRef.downloadUrl.addOnSuccessListener { Uri ->
        val imageURL = Uri.toString()
        Glide.with(view.context)
            .load(imageURL)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_baseline_help_center_24)
            )
            .into(view)
    }
}

@BindingAdapter("profileUrl")
fun bindImageProfile(view: ImageView, userId: String) {
    val imageRef = Firebase.storage.reference.child("profile").child(userId)
    imageRef.downloadUrl.addOnSuccessListener { Uri ->
        val imageURL = Uri.toString()
        Glide.with(view.context)
            .load(imageURL)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_baseline_help_center_24)
            )
            .into(view)
    }
}

/**
 * affiche le nom de l'utilisateur
 */
@BindingAdapter("welcome")
fun bindWelcome(view: TextView, username: String) {
    val text = "Bienvenue $username ! "
    view.text = text
}


/**
 * affiche le score moyen
 */
@BindingAdapter("avgRating")
fun bindAvgRating(view: MaterialRatingBar, avgRating: Double) {
    view.rating = avgRating.toFloat()
}

/**
 * affiche le nombre de votes sur la vue détail
 */
@BindingAdapter("numRatingDetail")
fun bindnumRatingDetail(view: TextView, numRating: Double) {
    view.text = numRating.toInt().toString()
}

/**
 * affiche le nombre de votes sur la vue liste
 */
@BindingAdapter("numRatingList")
fun bindnumRatingList(view: TextView, numRating: Double) {
    val text: String = "(" + numRating.toInt().toString() + ")"
    view.text = text
}

/**
 * affiche le bon label en fonction de si c'est un manga ou un anime
 */
@BindingAdapter("episodeChapterLabel")
fun bindEpisodeChapterLabel(view: TextView, mangaProperty: MangaProperty) {
    if (mangaProperty.isManga()) {
        view.text = "Nombre de chapitres: "

    } else {
        view.text = "Nombre d'épisodes: "
    }
}

/**
 * affiche le bon contenu en fonction de si c'est un manga ou un anime
 */
@BindingAdapter("episodeChapterDetail")
fun bindEpisodeChapterDetail(view: TextView, mangaProperty: MangaProperty) {
    if (mangaProperty.isManga()) {
        view.text = mangaProperty.chapters?.toInt().toString()

    } else {
        view.text = mangaProperty.episodes?.toInt().toString()
    }
}

/**
 * affiche la date correctement formatée
 */
@BindingAdapter("date")
fun bindDate(view: TextView, date: Date) {
    val dateFormat = SimpleDateFormat(
        "dd-MM-yyy HH:mm:ss", Locale.FRANCE
    )

    view.text = dateFormat.format(date)
}