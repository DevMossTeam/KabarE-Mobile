package com.devmoss.kabare.ui.reaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.FragmentDaftarBeritaBookmarkBinding
import com.devmoss.kabare.ui.reaksi.reaksiadapters.DaftarBeritaBookmarkAdapter
import com.devmoss.kabare.ui.reaksi.reaksiviewmodels.DaftarBeritaBookmarkViewModel

class DaftarBeritaBookmarkFragment : Fragment() {

    private var _binding: FragmentDaftarBeritaBookmarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var daftarBeritaBookmarkAdapter: DaftarBeritaBookmarkAdapter
    private val daftarBeritaBookmarkViewModel: DaftarBeritaBookmarkViewModel by activityViewModels()
    private val userId: String = "2" // Ganti dengan mekanisme mendapatkan user ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaftarBeritaBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()

        // Muat data bookmark untuk user
        daftarBeritaBookmarkViewModel.loadBeritaBookmark(userId)
    }

    private fun observeViewModel() {

        daftarBeritaBookmarkViewModel.isLoading.observe(viewLifecycleOwner) { progressBar(it) }

        daftarBeritaBookmarkViewModel.beritaBookmarkList.observe(viewLifecycleOwner) { beritaList ->
            if (beritaList.isNotEmpty()) {
                daftarBeritaBookmarkAdapter.updateList(beritaList)
                binding.belumBookmark.visibility = View.GONE
                binding.rvBeritaBookmark.visibility = View.VISIBLE
            } else {
                binding.belumBookmark.visibility = View.VISIBLE
                binding.rvBeritaBookmark.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerViews() {
        daftarBeritaBookmarkAdapter = DaftarBeritaBookmarkAdapter(
            emptyList(),
            object : DaftarBeritaBookmarkAdapter.OnItemClickListener {
                override fun onBeritaBookmarkClick(beritaBookmark: ListBerita) {
                    val navOptions = NavOptions.Builder()
                        .setEnterAnim(android.R.anim.slide_in_left)
                        .setExitAnim(android.R.anim.slide_out_right)
                        .build()
                    val bundle = Bundle().apply {
                        putString("beritaId", beritaBookmark.idBerita) // Kirim ID Berita
                    }
                    requireParentFragment().findNavController().navigate(
                        R.id.action_navigation_profil_to_detailArtikelFragment, bundle,navOptions)
                }
            })

        binding.rvBeritaBookmark.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = daftarBeritaBookmarkAdapter
        }
    }

    private fun progressBar(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progresBar.visibility = View.VISIBLE
                rvBeritaBookmark.visibility = View.GONE
                belumBookmark.visibility = View.GONE
            } else {
                progresBar.visibility = View.GONE
                rvBeritaBookmark.visibility = View.VISIBLE
                belumBookmark.visibility = View.VISIBLE
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
