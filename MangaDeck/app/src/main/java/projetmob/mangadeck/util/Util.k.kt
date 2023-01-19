package projetmob.mangadeck.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/**
 * Contient un ensemble de méthodes "outils"
 */

fun isCredentialsOk(mail: String, password: String, username : String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(mail)
        .matches() && isValidPassword(password) && isValidUsername(username)
}

fun isCredentialsOk(mail: String, password: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(mail)
        .matches() && isValidPassword(password)
}

/**
 * Vérifie la validité du mot de passe :
 * - un chiffre
 * - un caractère spécial
 * - une majuscule
 * - min 6 caractère
 */
fun isValidPassword(password: String?): Boolean {
    password?.let {
        val passwordPattern =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$"
        val passwordMatcher = Regex(passwordPattern)

        return passwordMatcher.find(password) != null
    } ?: return false
}

/**
 * Vérifie la validité du pseudo :
 * - que des chiffres et des lettres
 */
fun isValidUsername(username: String?): Boolean {
    username?.let {
        val usernamePattern =
            "[A-Za-z0-9_.-]+\$"
        val usernameMatcher = Regex(usernamePattern)

        return usernameMatcher.find(username) != null
    } ?: return false
}

/**
 * Cache le clavier d'un fragment
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

/**
 * Cache le clavier d'une activité
 */
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/**
 * Cache le clavier sur base du contexte
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}