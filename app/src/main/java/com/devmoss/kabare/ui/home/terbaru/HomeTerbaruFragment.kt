package com.devmoss.kabare.ui.home.terbaru

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResultBookmark
import com.devmoss.kabare.databinding.FragmentHomeTerbaruBinding
import com.devmoss.kabare.ui.bookmark.BookmarkViewModel
import com.devmoss.kabare.ui.home.terbaru.terbaruadapters.BeritaTerkaitAdapter
import com.devmoss.kabare.ui.home.terbaru.terbaruadapters.BeritaTerkiniAdapter
import com.devmoss.kabare.ui.home.terbaru.terbaruviewmodels.BeritaTerkaitViewModel
import com.devmoss.kabare.ui.home.terbaru.terbaruviewmodels.BeritaTerkiniViewModel

class HomeTerbaruFragment : Fragment() {

    private var _binding: FragmentHomeTerbaruBinding? = null
    private val binding get() = _binding!!
    private lateinit var beritaTerkiniAdapter: BeritaTerkiniAdapter
    private lateinit var beritaTerkaitAdapter: BeritaTerkaitAdapter
    private val beritaTerkiniViewModel: BeritaTerkiniViewModel by activityViewModels()
    private val beritaTerkaitViewModel: BeritaTerkaitViewModel by activityViewModels()
    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    val userIdLogin = "3"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeTerbaruBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Memastikan berita hanya dimuat sekali
        beritaTerkiniViewModel.loadBeritaTerkini()
        beritaTerkaitViewModel.loadBeritaTerkait("", emptyList()) // Muat berita terkait saat fragment dibuat

        setupRecyclerViews()
        observeViewModel()
    }

    private fun observeViewModel() {
        beritaTerkiniViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showShimmerEffect(isLoading)
        })

        beritaTerkiniViewModel.beritaTerkiniList.observe(viewLifecycleOwner, Observer { beritaList ->
            if (beritaList.isNotEmpty()) {
                // Update list berita
                beritaTerkiniAdapter.updateList(beritaList)
                // Ambil daftar kategori untuk berita terkait
                val kategoriList = beritaList.mapNotNull { it.kategori }.distinct()
                if (kategoriList.isNotEmpty()) {
                    kategoriList.forEach { kategori ->
                        beritaTerkaitViewModel.loadBeritaTerkait(kategori, beritaList)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Tidak ada berita terbaru.", Toast.LENGTH_SHORT).show()
            }

        })

        beritaTerkaitViewModel.beritaTerkaitList.observe(viewLifecycleOwner, Observer { beritaTerkait ->
            if (beritaTerkait.isNotEmpty()) {
                beritaTerkaitAdapter.updateList(beritaTerkait)
            } else {
                Toast.makeText(requireContext(), "Tidak ada berita terkait.", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupRecyclerViews() {
        beritaTerkiniAdapter = BeritaTerkiniAdapter(emptyList(), object : BeritaTerkiniAdapter.OnItemClickListener {
            override fun onArticleTerkiniClick(beritaTerkini: ListBerita) {
                val bundleBeritaTerkini = Bundle().apply {
                    putParcelable("beritaArgument", beritaTerkini)
                }
                findNavController().navigate(
                    R.id.action_navigation_home_to_detailArtikelFragment,
                    bundleBeritaTerkini
                )
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onBookmarkBeritaTerkiniClick(beritaTerkini: ListBerita) {
                val beritaId = beritaTerkini.idBerita

                if (beritaId != null) {
                    val bookmark = ResultBookmark(userIdLogin, beritaId)

                    // Menambahkan atau menghapus bookmark (toggle)
                    bookmarkViewModel.toggleBookmark(bookmark)

                    // Toggle status bookmark antara 0 dan 1
                    beritaTerkini.isBookmarked = if (beritaTerkini.isBookmarked == 0) 1 else 0

//                    // Tampilkan toast sesuai status bookmark
                    Toast.makeText(
                        context,
                        if (beritaTerkini.isBookmarked == 1) "Bookmark berhasil ditambahkan." else "Bookmark berhasil dihapus.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Perbarui adapter untuk menampilkan ikon yang benar
                    beritaTerkiniAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "ID berita tidak valid.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.rvBeritaTerkini.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = beritaTerkiniAdapter
        }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        beritaTerkaitAdapter = BeritaTerkaitAdapter(emptyList(), object : BeritaTerkaitAdapter.OnItemClickListener {

            override fun onArticleTerkaitClick(beritaTerkait: ListBerita) {
                val bundleBeritaTerkait = Bundle().apply {
                    putParcelable("beritaArgument", beritaTerkait)
                }
                findNavController().navigate(
                    R.id.action_navigation_home_to_detailArtikelFragment,
                    bundleBeritaTerkait
                )
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onBookmarkBeritaTerkaitClick(beritaTerkait: ListBerita) {
                val beritaId = beritaTerkait.idBerita

                if (beritaId != null) {
                    val bookmark = ResultBookmark(userIdLogin, beritaId)

                    // Menambahkan atau menghapus bookmark (toggle)
                    bookmarkViewModel.toggleBookmark(bookmark)

                    // Toggle status bookmark antara 0 dan 1
                    beritaTerkait.isBookmarked = if (beritaTerkait.isBookmarked == 0) 1 else 0

//                    // Tampilkan toast sesuai status bookmark
                    Toast.makeText(
                        context,
                        if (beritaTerkait.isBookmarked == 1) "Bookmark berhasil ditambahkan." else "Bookmark berhasil dihapus.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Perbarui adapter untuk menampilkan ikon yang benar
                    beritaTerkaitAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "ID berita tidak valid.", Toast.LENGTH_SHORT).show()
                }
            }
        })


        binding.rvBeritaTerkait.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = beritaTerkaitAdapter
        }
    }

    private fun showShimmerEffect(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerBeritaTerkini.startShimmer()
            binding.shimmerBeritaTerkini.visibility = View.VISIBLE
            binding.rvBeritaTerkini.visibility = View.GONE
            binding.rvBeritaTerkait.visibility = View.GONE
            binding.tvBeritaTeratas.visibility = View.GONE
            binding.lineTeratas.visibility = View.GONE
        } else {
            binding.shimmerBeritaTerkini.stopShimmer()
            binding.shimmerBeritaTerkini.visibility = View.GONE
            binding.rvBeritaTerkini.visibility = View.VISIBLE
            binding.rvBeritaTerkait.visibility = View.VISIBLE
            binding.tvBeritaTeratas.visibility = View.VISIBLE
            binding.lineTeratas.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
