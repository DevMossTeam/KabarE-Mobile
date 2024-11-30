package com.devmoss.kabare.ui.home.populer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResultBookmark
import com.devmoss.kabare.data.model.Tag
import com.devmoss.kabare.databinding.FragmentHomePopulerBinding
import com.devmoss.kabare.ui.bookmark.BookmarkViewModel
import com.devmoss.kabare.ui.home.populer.populeradapters.BeritaPopulerAdapter
import com.devmoss.kabare.ui.home.populer.populeradapters.BeritaRekomendasiAdapter
import com.devmoss.kabare.ui.home.populer.populeradapters.TagPopulerAdapter
import com.devmoss.kabare.ui.home.populer.populerviewmodels.BeritaPopulerViewModel
import com.devmoss.kabare.ui.home.populer.populerviewmodels.BeritaRekomendasiViewModel
import com.devmoss.kabare.ui.home.populer.populerviewmodels.TagPopulerViewModel
import kotlinx.coroutines.*

class HomePopulerFragment : Fragment() {
    private var _binding: FragmentHomePopulerBinding? = null
    private val binding get() = _binding!!
    private lateinit var beritaPopulerAdapter: BeritaPopulerAdapter
    private lateinit var tagPopulerAdapter: TagPopulerAdapter
    private lateinit var beritaRekomendasiAdapter: BeritaRekomendasiAdapter

    private val beritaPopulerViewModel: BeritaPopulerViewModel by activityViewModels()
    private val tagPopulerViewModel: TagPopulerViewModel by activityViewModels()
    private val beritaRekomendasiViewModel: BeritaRekomendasiViewModel by activityViewModels()
    private val bookmarkViewModel: BookmarkViewModel by viewModels()

    val userIdLogin = "3"

    private val autoSlideJob = Job()
    private val autoSlideScope = CoroutineScope(Dispatchers.Main + autoSlideJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePopulerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeViewModel()
        beritaPopulerViewModel.loadBeritaPopuler()
        tagPopulerViewModel.loadTagPopuler()
        beritaRekomendasiViewModel.loadBeritaRekomendasi(userId = "2")

        setupNavigationButtons()
        startAutoSlide()
    }

    private fun setupRecyclerViews() {
        beritaPopulerAdapter = BeritaPopulerAdapter(emptyList(), object : BeritaPopulerAdapter.OnItemClickListener {
            override fun onArticlePopulerClick(berita: ListBerita) {
                val bundleBeritaPopuler = Bundle().apply { putParcelable("beritaArgument", berita) }
                findNavController().navigate(R.id.action_navigation_home_to_detailArtikelFragment, bundleBeritaPopuler)
            }

            override fun onBookmarkBeritaPopulerClick(beritaPopuler: ListBerita) {
                val userId = "3" // Sesuaikan dengan user ID yang login
                val beritaId = beritaPopuler.idBerita

                if (beritaId != null) {
                    val bookmark = ResultBookmark(userId, beritaId)

                    // Menambahkan atau menghapus bookmark (toggle)
                    bookmarkViewModel.toggleBookmark(bookmark)

                    // Toggle status bookmark antara 0 dan 1
                    beritaPopuler.isBookmarked = if (beritaPopuler.isBookmarked == 0) 1 else 0

//                    // Tampilkan toast sesuai status bookmark
                    Toast.makeText(
                        context,
                        if (beritaPopuler.isBookmarked == 1) "Bookmark berhasil ditambahkan." else "Bookmark berhasil dihapus.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Perbarui adapter untuk menampilkan ikon yang benar
                    beritaPopulerAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "ID berita tidak valid.", Toast.LENGTH_SHORT).show()
                }
            }
        })


        beritaRekomendasiAdapter = BeritaRekomendasiAdapter(emptyList(), object : BeritaRekomendasiAdapter.OnItemClickListener {
            override fun onArticleRekomendasiClick(berita: ListBerita) {
                val bundleBeritaRekomendasi = Bundle().apply { putParcelable("beritaArgument", berita) }
                findNavController().navigate(R.id.action_navigation_home_to_detailArtikelFragment, bundleBeritaRekomendasi)
            }

            override fun onBookmarkBeritaRekomendasiClick(beritaRekomendasi: ListBerita) {
                val beritaId = beritaRekomendasi.idBerita

                if (beritaId != null) {
                    val bookmark = ResultBookmark(userIdLogin, beritaId)

                    // Menambahkan atau menghapus bookmark (toggle)
                    bookmarkViewModel.toggleBookmark(bookmark)

                    // Toggle status bookmark antara 0 dan 1
                    beritaRekomendasi.isBookmarked = if (beritaRekomendasi.isBookmarked == 0) 1 else 0

//                    // Tampilkan toast sesuai status bookmark
                    Toast.makeText(
                        context,
                        if (beritaRekomendasi.isBookmarked == 1) "Bookmark berhasil ditambahkan." else "Bookmark berhasil dihapus.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Perbarui adapter untuk menampilkan ikon yang benar
                    beritaRekomendasiAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "ID berita tidak valid.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        tagPopulerAdapter = TagPopulerAdapter(emptyList(), object : TagPopulerAdapter.OnItemClickListener {
            override fun onTagPopulerClick(tagPopuler: Tag) {
                Toast.makeText(requireContext(), "Tag Clicked: ${tagPopuler.namaTag}", Toast.LENGTH_SHORT).show()
            }
        })

        binding.rvTagPopuler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tagPopulerAdapter
        }

        binding.rvBeritaTeratasPopuler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = beritaPopulerAdapter
        }

        binding.rvBeritaRekomendasi.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = beritaRekomendasiAdapter
        }
    }

    private fun observeViewModel() {
        beritaPopulerViewModel.isLoading.observe(viewLifecycleOwner) { showShimmerEffect(it) }

        beritaPopulerViewModel.beritaPopulerList.observe(viewLifecycleOwner) { beritaPopulerList ->
            if (beritaPopulerList.isNotEmpty()) {
                beritaPopulerAdapter.updateList(beritaPopulerList)
            } else {
                showToast("Tidak ada berita populer.")
            }
        }

        beritaRekomendasiViewModel.beritaRekomendasiList.observe(viewLifecycleOwner) { beritaRekomendasiList ->
            if (beritaRekomendasiList.isNotEmpty()) {
                beritaRekomendasiAdapter.updateList(beritaRekomendasiList)
            } else {
                showToast("Tidak ada berita rekomendasi.")
            }
        }

        tagPopulerViewModel.tagPopulerList.observe(viewLifecycleOwner) { tagPopulerList ->
            if (tagPopulerList.isNotEmpty()) {
                tagPopulerAdapter.updateList(tagPopulerList)
            } else {
                showToast("Tidak ada tag populer.")
            }
        }
    }

    private fun setupNavigationButtons() {
        binding.btnNext.setOnClickListener {
            scrollRecyclerView(binding.rvBeritaRekomendasi, true)
        }

        binding.btnPrev.setOnClickListener {
            scrollRecyclerView(binding.rvBeritaRekomendasi, false)
        }
    }

    private fun startAutoSlide() {
        autoSlideScope.launch {
            while (isActive) {
                delay(4000)
                if (isAdded) {
                    scrollRecyclerView(binding.rvBeritaRekomendasi, true)
                }
            }
        }
    }

    private fun scrollRecyclerView(recyclerView: RecyclerView, isNext: Boolean) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val currentPosition = layoutManager.findFirstVisibleItemPosition()
        val totalItemCount = beritaRekomendasiAdapter.itemCount

        val targetPosition = when {
            isNext && currentPosition < totalItemCount - 1 -> currentPosition + 1
            !isNext && currentPosition > 0 -> currentPosition - 1
            isNext -> 0 // Reset to first item if at the end
            else -> totalItemCount - 1 // Move to last item if at the beginning
        }

        recyclerView.smoothScrollToPosition(targetPosition)
    }

    private fun showShimmerEffect(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                shimmerBeritaPopuler.startShimmer()
                shimmerBeritaPopuler.visibility = View.VISIBLE
                rvBeritaTeratasPopuler.visibility = View.GONE
                tvLabelTerpopuler.visibility = View.GONE
                rvTagPopuler.visibility = View.GONE
                rvBeritaRekomendasi.visibility = View.GONE
                btnNext.visibility = View.GONE
                btnPrev.visibility = View.GONE
                line1.visibility = View.GONE
                line2.visibility = View.GONE
                tvLabelTerpopuler.visibility = View.GONE
                tvLabelDisarankan.visibility = View.GONE
            } else {
                shimmerBeritaPopuler.stopShimmer()
                shimmerBeritaPopuler.visibility = View.GONE
                rvBeritaTeratasPopuler.visibility = View.VISIBLE
                tvLabelTerpopuler.visibility = View.VISIBLE
                rvTagPopuler.visibility = View.VISIBLE
                rvBeritaRekomendasi.visibility = View.VISIBLE
                btnNext.visibility = View.VISIBLE
                btnPrev.visibility = View.VISIBLE
                line1.visibility = View.VISIBLE
                line2.visibility = View.VISIBLE
                tvLabelTerpopuler.visibility = View.VISIBLE
                tvLabelDisarankan.visibility = View.VISIBLE
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoSlideScope.cancel()
        _binding = null
    }
}
