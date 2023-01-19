package projetmob.mangadeck.list

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import projetmob.mangadeck.model.MangaProperty

class ListViewModel(application: Application) : AndroidViewModel(application) {

    // données de le firebase
    private val _properties = MutableLiveData<List<MangaProperty>>()

    val properties: LiveData<List<MangaProperty>>
        get() = _properties

    // query permettant de récupérer les mangas
    private val _query = MutableLiveData<Query>()

    val query: LiveData<Query>
        get() = _query

    // indique si l'utilisateur veut voir le détail
    private val _detail = MutableLiveData<MangaProperty>()

    val detail: LiveData<MangaProperty>
        get() = _detail

    var username : String = "?"

    init {
        getMangaProperties()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        username = user.displayName.toString()
    }

    /**
     * récupère les données de firebase
     */
    private fun getMangaProperties() {
        // Récupérer juste les données
        _query.value = FirebaseFirestore.getInstance().collection("mangas")
    }

    /**
     * affiche le détail du manga sélectionné
     */
    fun displayMangaDetail(manga : MangaProperty?){
        _detail.value = manga
    }

    /**
     * remets à zéro l'argument après navigation
     */
    fun displayMangaDetailComplete() {
        _detail.value = null
    }

    /**
     * factory de ListViewModel
     */
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
                return ListViewModel(application) as T
            }
            throw IllegalArgumentException("Cannot construct viewModel")
        }
    }

}