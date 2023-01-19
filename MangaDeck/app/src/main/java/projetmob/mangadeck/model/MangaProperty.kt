package projetmob.mangadeck.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// Permet d'envoyer l'info en entier à travers les fragments
@Parcelize
data class MangaProperty(
    var id: String? = null,
    var title: String? = null,
    var status: String? = null,
    var episodes: Double? = 0.0,
    var chapters: Double? = 0.0,
    var synopsis: String? = null,
    var type: String? = null,
    var image: String? = null,
    var numRating: Double? = 0.0,
    var avgRating: Double? = 0.0,
    var category: String? = null
) : Parcelable {
    fun isManga() : Boolean {
        return type == "Manga"
    }
}
