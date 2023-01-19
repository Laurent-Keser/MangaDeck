package projetmob.mangadeck.login

import android.app.Application
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import projetmob.mangadeck.util.hideKeyboard
import projetmob.mangadeck.util.isCredentialsOk


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // instance d'authentification
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // indique si l'utilisateur peut se connecter
    private val _authenticated = MutableLiveData<Boolean>()

    val authenticated: LiveData<Boolean>
        get() = _authenticated

    // indique si l'utilisateur veut s'inscrire
    private val _register = MutableLiveData<Boolean>()

    val register: LiveData<Boolean>
        get() = _register

    // indique le message d'erreur à afficher
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    init {
        checkUserConnected()
    }

    /**
     * vérifie si l'utilisateur est déjà connecté
     */
    fun checkUserConnected() {
        _authenticated.value = FirebaseAuth.getInstance().currentUser != null
    }

    /**
     * tente de connecter l'utilisateur sur base de l'email et du mot de passe
     */
    fun loginUser(emailEditable: Editable, passwordEditable: Editable) {
        // Conversion des données
        val email = emailEditable.toString()
        val password = passwordEditable.toString()

        if (!isCredentialsOk(email, password)) {
            _error.value = "L'email et/ou le mot de passe entrés sont incorrects"
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authenticated.value = true
                    } else{
                        _error.value = "Impossible de se connecter au service"
                    }
                }
        }
    }

    /**
     * permet le passage à la vue d'inscription
     */
    fun registerUser(){
        _register.value = true
    }

    /**
     * remets à zéro l'argument après navigation
     */
    fun loginComplete() {
        _authenticated.value = false
    }

    /**
     * remets à zéro l'argument après navigation
     */
    fun registerComplete() {
        _register.value = false
    }

    /**
     * remets à zéro l'affichage de l'erreur
     */
    fun showErrorComplete() {
        _error.value = null
    }

    /**
     * factory de loginViewModel
     */
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(application) as T
            }
            throw IllegalArgumentException("Cannot construct viewModel")
        }
    }
}

