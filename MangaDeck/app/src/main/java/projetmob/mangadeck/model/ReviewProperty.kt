package projetmob.mangadeck.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ReviewProperty(
    var userId: String? = null,
    var mangaId : String ? = null,
    var username: String? = "",
    var rating: Double = 0.0,
    var review: String? = "",
    @ServerTimestamp
    var date: Date? = null
) : Parcelable