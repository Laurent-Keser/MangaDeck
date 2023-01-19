package projetmob.mangadeck.detail

import android.app.AlertDialog
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
import projetmob.mangadeck.adapter.MangaAdapter
import projetmob.mangadeck.databinding.DialogRatingBinding
import projetmob.mangadeck.databinding.FragmentDetailBinding
import timber.log.Timber

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var adapter: DetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Layout
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        // Modèle
        val mangaProperty = DetailFragmentArgs.fromBundle(requireArguments()).mangaProperty
        viewModel =
            ViewModelProvider(
                this,
                DetailViewModel.Factory(mangaProperty, requireActivity().application)
            ).get(
                DetailViewModel::class.java
            )

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Review
        viewModel.reviewDialog.observe(viewLifecycleOwner, {
            if (it) {
                reviewDialog(inflater, container)
                viewModel.showDialogComplete()
            }
        })

        // setup recyclerView
        viewModel.query.observe(viewLifecycleOwner, {
            if (it != null) {

                adapter = DetailAdapter(it, DetailAdapter.OnClickListener {
                })

                binding.reviewRecycler.adapter = adapter
                adapter.startListening()
            }
        })

        binding.reviewRecycler.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        return binding.root
    }

    private fun reviewDialog(inflater: LayoutInflater, container: ViewGroup?) {
        val builder = AlertDialog.Builder(requireContext())
        val reviewDialogBinding: DialogRatingBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_rating,
            container,
            false
        )

        with(builder){
            setTitle("Ajouter un avis")

            setNegativeButton("Retour"){dialog, which ->
            }

            setPositiveButton("Enregistrer"){ dialog, which ->
                // Enregistrer
                viewModel.addReview(reviewDialogBinding.mangaFormRating.rating, reviewDialogBinding.mangaFormText.text)
            }

            setView(reviewDialogBinding.root)
            show()
        }

    }

    override fun onStop() {
        super.onStop()

        // FirestoreAdapter
        adapter.stopListening()
    }


}