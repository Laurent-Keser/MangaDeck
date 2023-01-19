package projetmob.mangadeck.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import projetmob.mangadeck.R
import projetmob.mangadeck.adapter.DetailAdapter
import projetmob.mangadeck.adapter.ProfileAdapter
import projetmob.mangadeck.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: ProfileAdapter
    private val gallery = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Layout
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        // Modèle
        viewModel =
            ViewModelProvider(this, ProfileViewModel.Factory(requireActivity().application)).get(
                ProfileViewModel::class.java
            )

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Image
        viewModel.image.observe(viewLifecycleOwner, {
            if (it) {
                uploadImage()
                viewModel.addImageComplete()
            }
        })

        // setup recyclerView
        viewModel.query.observe(viewLifecycleOwner, {
            if (it != null) {

                adapter = ProfileAdapter(it, ProfileAdapter.OnClickListener {
                })

                binding.profileReviewRecycler.adapter = adapter
                adapter.startListening()
            }
        })

        binding.profileReviewRecycler.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        return binding.root
    }

    /**
     * envoie l'image sur la DB et la place en photo de profil
     */
    private fun uploadImage() {
        // Demander à prendre l'image
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, gallery)
    }

    /**
     * envoyer la photo après choix
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == gallery && resultCode == RESULT_OK && data != null && data.data != null) {
            val filepath = data.data
            if (filepath != null) {
                viewModel.uploadImage(filepath)
            }
        }
    }

    override fun onStop() {
        super.onStop()

        // FirestoreAdapter
        adapter.stopListening()
    }
}