package projetmob.mangadeck.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.navigation.NavigationView
import projetmob.mangadeck.R
import projetmob.mangadeck.adapter.MangaAdapter
import projetmob.mangadeck.databinding.FragmentListBinding
import projetmob.mangadeck.login.LoginFragmentDirections
import projetmob.mangadeck.util.hideKeyboard
import timber.log.Timber

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: MangaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Layout
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false
        )

        // Modèle
        viewModel =
            ViewModelProvider(this, ListViewModel.Factory(requireActivity().application)).get(
                ListViewModel::class.java
            )

        binding.viewModel = viewModel

        // setup recyclerView
        viewModel.query.observe(viewLifecycleOwner, { it ->
            if (it != null) {

                adapter = MangaAdapter(it, MangaAdapter.OnClickListener {
                    viewModel.displayMangaDetail(it)
                })

                binding.mangaList.adapter = adapter

                adapter.startListening()
            }
        })

        binding.mangaList.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        // Navigation
        viewModel.detail.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController().navigate(ListFragmentDirections.actionListFragmentToDetailFragment(it))
                viewModel.displayMangaDetailComplete()
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
        logoutItem.isVisible = true
        profileItem.isVisible = true
    }

    override fun onStop() {
        super.onStop()

        // FirestoreAdapter
        adapter.stopListening()
    }



}