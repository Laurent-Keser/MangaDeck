package projetmob.mangadeck.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import projetmob.mangadeck.R
import projetmob.mangadeck.databinding.FragmentRegisterBinding
import projetmob.mangadeck.util.hideKeyboard

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Layout
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false
        )

        // Modèle
        viewModel =
            ViewModelProvider(this, RegisterViewModel.Factory(requireActivity().application)).get(
                RegisterViewModel::class.java
            )

        binding.viewModel = viewModel

        // vérification d'inscription
        viewModel.registered.observe(viewLifecycleOwner, {
            if(it){
                Toast.makeText(activity, "Inscription réussie", Toast.LENGTH_SHORT).show()
                viewModel.registerComplete()
                hideKeyboard()
                requireActivity().onBackPressed()
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
}