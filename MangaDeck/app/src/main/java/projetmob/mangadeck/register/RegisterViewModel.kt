package projetmob.mangadeck.register

import android.app.Application
import android.text.Editable
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import projetmob.mangadeck.util.isCredentialsOk

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    // instance d'authentification
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // indique si l'utilisateur est inscrit
    private val _registered = MutableLiveData<Boolean>()

    val registered: LiveData<Boolean>
        get() = _registered

    // indique le message d'erreur à afficher
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    /**
     * tente d'inscrire l'utilisateur sur base de l'email, du mot de passe et du pseudo
     */
    fun registerUser(emailEditable: Editable, passwordEditable: Editable, usernameEditable : Editable) {
        // conversion des données
        val email = emailEditable.toString()
        val password = passwordEditable.toString()
        val username = usernameEditable.toString()

        if (!isCredentialsOk(email, password, username)) {
            _error.value = "Les données entrées sont incorrectes"
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // Update pseudo
                        val profileUpdate = userProfileChangeRequest {
                            displayName = username
                        }
                        auth.currentUser!!.updateProfile(profileUpdate).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _registered.value = true
                            }
                        }
                    } else {
                        _error.value = "Impossible de se connecter au service"
                    }
                }
        }
    }

    /**
     * remets à zéro l'argument après navigation
     */
    fun registerComplete() {
        _registered.value = false
    }

    /**
     * remets à zéro l'affichage de l'erreur
     */
    fun showErrorComplete() {
        _error.value = null
    }

    /**
     * factory de registerViewModel
     */
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(application) as T
            }
            throw IllegalArgumentException("Cannot construct viewModel")
        }
    }
}