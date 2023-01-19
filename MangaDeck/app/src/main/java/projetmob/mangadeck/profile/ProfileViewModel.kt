package projetmob.mangadeck.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import timber.log.Timber

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // instance d'authentification
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // instance de base de donnée
    private val db = Firebase.firestore

    // instance de storage
    private val storage = Firebase.storage

    // query permettant de récupérer les reviews
    private val _query = MutableLiveData<Query>()

    val query: LiveData<Query>
        get() = _query

    // utilisateur courant
    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser>
        get() = _user

    // indique si l'utilisateur veut changer d'avatar
    private val _image = MutableLiveData<Boolean>()

    val image: LiveData<Boolean>
        get() = _image

    init{
        _user.value = auth.currentUser!!
        getReviewProperties(user.value!!.uid)
    }

    /**
     * récupère les données de firebase
     */
    private fun getReviewProperties(userId: String) {
        _query.value = FirebaseFirestore.getInstance().collection("users").document(userId)
            .collection("review")
            .orderBy("date", Query.Direction.DESCENDING)
    }

    /**
     * indique que l'on veut uploader une image
     */
    fun addImage() {
        _image.value = true
    }

    fun addImageComplete() {
        _image.value = false
    }

    /**
     * upload l'image dans la DB
     */
    fun uploadImage(path: Uri) {
        val userId = user.value!!.uid
        storage.reference.child("profile").child(userId).putFile(path).addOnSuccessListener {
            val user = _user.value
            _user.value = user
            getReviewProperties(user!!.uid)
        }
    }

    /**
     * factory de profileViewModel
     */
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(application) as T
            }
            throw IllegalArgumentException("Cannot construct viewModel")
        }
    }
}