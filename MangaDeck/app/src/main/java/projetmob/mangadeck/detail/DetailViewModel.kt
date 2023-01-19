package projetmob.mangadeck.detail

import android.app.Application
import android.text.Editable
import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import projetmob.mangadeck.model.MangaProperty
import timber.log.Timber
import java.util.ArrayList

class DetailViewModel(mangaProperty: MangaProperty, application: Application) :
    AndroidViewModel(application) {

    // instance d'authentification
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // instance de base de donnée
    private val db = Firebase.firestore

    // query permettant de récupérer les reviews
    private val _query = MutableLiveData<Query>()

    val query: LiveData<Query>
        get() = _query

    // manga sélectionné
    private val _mangaProperty = MutableLiveData<MangaProperty>()
    val mangaProperty: LiveData<MangaProperty>
        get() = _mangaProperty

    private val _reviewDialog = MutableLiveData<Boolean>()
    val reviewDialog: LiveData<Boolean>
        get() = _reviewDialog

    init {
        _mangaProperty.value = mangaProperty
        getReviewProperties(mangaProperty.id.toString())
    }

    /**
     * récupère les données de le firebase
     */
    private fun getReviewProperties(mangaId: String) {
        _query.value = FirebaseFirestore.getInstance().collection("reviews").document(mangaId)
            .collection("review")
            .orderBy("date", Query.Direction.DESCENDING)
    }

    fun showDialog() {
        _reviewDialog.value = true
    }

    fun showDialogComplete() {
        _reviewDialog.value = false
    }

    /**
     * ajouter une review pour le manga/anime
     */
    fun addReview(rating: Float, reviewEditable: Editable) {
        val userId = auth.currentUser!!.uid
        val username = auth.currentUser!!.displayName
        val mangaId = mangaProperty.value!!.id!!
        var reviewText: String = reviewEditable.toString()
        val rating = rating.toDouble()

        if (reviewEditable.isEmpty()) {
            reviewText = "Pas de commentaire"
        }

        // Update DB
        val review = hashMapOf(
            "userId" to userId,
            "username" to username,
            "mangaId" to mangaId,
            "date" to Timestamp.now(),
            "rating" to rating,
            "review" to reviewText
        )

        createInReviews(mangaId, userId, review, rating)
        createInUsers(mangaId, userId, review, rating)
    }

    /**
     * crée le document dans la collection reviews
     */
    private fun createInReviews(mangaId : String, userId : String, review : HashMap<String, Comparable<*>?>, rating : Double){
        val reviewsRef = db.collection("reviews").document(mangaId)

        // Créer le document dans /reviews
        reviewsRef.get().addOnCompleteListener {
            if (!it.result.exists()) {
                val manga = hashMapOf(
                    "reviewSum" to 0,
                    "reviewCount" to 0,
                )
                db.collection("reviews").document(mangaId).set(manga)
            }
        }

        // créer l'avis dans /reviews/mangaId
        val reviewsDocumentRef =
            db.collection("reviews").document(mangaId).collection("review").document(userId)

        reviewsDocumentRef.get().addOnCompleteListener {
            // Le document existe : on update
            if (it.result.exists()) {
                val previousRating: Double = it.result.get("rating") as Double
                reviewsDocumentRef.set(review).addOnSuccessListener {
                    reviewsRef.update("reviewSum", FieldValue.increment(-previousRating))
                    reviewsRef.update("reviewSum", FieldValue.increment(rating))

                    reviewsRef.get().addOnCompleteListener {
                        updateDBAndUI(it, mangaId)
                    }
                }
            }

            // si lee document n'existe pas alors on le crée
            else {
                reviewsDocumentRef.set(review).addOnSuccessListener {
                    reviewsRef.update("reviewCount", FieldValue.increment(1))
                    reviewsRef.update("reviewSum", FieldValue.increment(rating))

                    reviewsRef.get().addOnCompleteListener {
                        updateDBAndUI(it, mangaId)
                    }
                }
            }
        }
    }

    /**
     * crée le document dans la collection users
     */
    private fun createInUsers(mangaId : String, userId : String, review : HashMap<String, Comparable<*>?>, rating : Double){
        val usersRef = db.collection("users").document(userId)

        // Créer le document dans /users
        usersRef.get().addOnCompleteListener {
            if (!it.result.exists()) {
                val data = HashMap<String, Any>()
                db.collection("users").document(userId).set(data)
            }
        }

        // Créer l'avis dans /users/userId
        val usersDocumentRef =
            db.collection("users").document(userId).collection("review").document(mangaId)

        usersDocumentRef.get().addOnCompleteListener {
            usersDocumentRef.set(review)
        }
    }


    /**
     * mets à jour l'entité de manga et l'interface
     */
    private fun updateDBAndUI(task: Task<DocumentSnapshot>, mangaId: String) {
        val reviewSum = task.result.get("reviewSum") as Double
        val reviewCount = task.result.get("reviewCount") as Long
        val avgRating = reviewSum.div(reviewCount)

        db.collection("mangas").document(mangaId).update("avgRating", avgRating)
        db.collection("mangas").document(mangaId)
            .update("numRating", reviewCount)

        val manga = _mangaProperty.value!!
        manga.avgRating = avgRating
        manga.numRating = reviewCount.toDouble()
        _mangaProperty.value = manga

        getReviewProperties(mangaId)
    }

    /**
     * factory de DetailViewModel
     */
    class Factory(private val mangaProperty: MangaProperty, private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(mangaProperty, application) as T
            }
            throw IllegalArgumentException("Cannot construct viewModel")
        }
    }
}