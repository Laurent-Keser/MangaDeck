package projetmob.mangadeck.login

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import projetmob.mangadeck.R
import projetmob.mangadeck.databinding.FragmentLoginBinding
import projetmob.mangadeck.util.hideKeyboard

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Layout
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        // Modèle
        viewModel =
            ViewModelProvider(this, LoginViewModel.Factory(requireActivity().application)).get(
                LoginViewModel::class.java
            )

        binding.viewModel = viewModel

        // vérification de connexion
        viewModel.authenticated.observe(viewLifecycleOwner, {
            if(it){
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToListFragment())
                viewModel.loginComplete()
                hideKeyboard()
            }
        })

        // vérification d'inscription
        viewModel.register.observe(viewLifecycleOwner, {
            if(it){
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
                viewModel.registerComplete()
                hideKeyboard()
            }
        })

        // message d'erreur
        viewModel.error.observe(viewLifecycleOwner, {
            if(it != null){
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                viewModel.showErrorComplete()
                hideKeyboard()
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // configuration du drawer
        val navigationView: NavigationView = requireActivity().findViewById(R.id.navView)
        val menuNav = navigationView.menu
        val logoutItem = menuNav.findItem(R.id.logoutButton)
        val profileItem = menuNav.findItem(R.id.profileFragment)
        logoutItem.isVisible = false
        profileItem.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkUserConnected()
    }

}