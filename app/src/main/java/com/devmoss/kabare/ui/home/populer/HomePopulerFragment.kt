package com.devmoss.kabare.ui.home.populer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResultBookmark
import com.devmoss.kabare.data.model.Tag
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.databinding.FragmentHomePopulerBinding
import com.devmoss.kabare.ui.reaksi.reaksiviewmodels.BookmarkViewModel
import com.devmoss.kabare.ui.home.populer.populeradapters.BeritaPopulerAdapter
import com.devmoss.kabare.ui.home.populer.populeradapters.BeritaRekomendasiAdapter
import com.devmoss.kabare.ui.home.populer.populeradapters.KomentarTerbanyakAdapter
import com.devmoss.kabare.ui.home.populer.populeradapters.TagPopulerAdapter
import com.devmoss.kabare.ui.home.populer.populerviewmodels.BeritaPopulerViewModel
import com.devmoss.kabare.ui.home.populer.populerviewmodels.BeritaRekomendasiViewModel
import com.devmoss.kabare.ui.home.populer.populerviewmodels.KomentarTerbanyakViewModel
import com.devmoss.kabare.ui.home.populer.populerviewmodels.TagPopulerViewModel
import com.devmoss.kabare.ui.search.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomePopulerFragment : Fragment() {
    private var _binding: FragmentHomePopulerBinding? = null
    private val binding get() = _binding!!
    private lateinit var beritaPopulerAdapter: BeritaPopulerAdapter
    private lateinit var tagPopulerAdapter: TagPopulerAdapter
    private lateinit var beritaRekomendasiAdapter: BeritaRekomendasiAdapter
    private lateinit var komentarTerbanyakAdapter: KomentarTerbanyakAdapter

    private val beritaPopulerViewModel: BeritaPopulerViewModel by activityViewModels()
    private val tagPopulerViewModel: TagPopulerViewModel by activityViewModels()
    private val beritaRekomendasiViewModel: BeritaRekomendasiViewModel by activityViewModels()
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()
    private val komentarTerbanyakViewModel: KomentarTerbanyakViewModel by activityViewModels()

    private var beritaPopuler: List<ListBerita> = emptyList()
    private var beritaRekomendasi: List<ListBerita> = emptyList()
    private var komentarTerbanyak: List<ListBerita> = emptyList()
    private var tagPopuler: List<Tag> = emptyList()
    private val autoSlideJob = Job()
    private val autoSlideScope = CoroutineScope(Dispatchers.Main + autoSlideJob)
    private val searchViewModel: SearchViewModel by activityViewModels()

    //mengambil user id
    private lateinit var userRepository: UserRepository
    private var userId: String? = null


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

        setupNavigationButtons()
        startAutoSlide()

        // Setup SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshContent()
        }
        // Inisialisasi UserRepository
        userRepository = UserRepository(requireContext())
        userId = userRepository.getUserUid() ?: run {
            Toast.makeText(requireContext(), "User belum login!", Toast.LENGTH_SHORT).show()
            return // Jika userId null, hentikan proses lebih lanjut
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomePopulerFragment", "onResume: Memuat data jika kosong")
        if (beritaPopuler.isEmpty()) {
            Log.d("HomePopulerFragment", "Memuat berita populer")
            beritaPopulerViewModel.loadBeritaPopuler()
        }
        if (tagPopuler.isEmpty()) {
            Log.d("HomePopulerFragment", "Memuat tag populer")
            tagPopulerViewModel.loadTagPopuler()
        }
        if (beritaRekomendasi.isEmpty()) {
            Log.d("HomePopulerFragment", "Memuat berita rekomendasi")
            beritaRekomendasiViewModel.loadBeritaRekomendasi(  userId ?: "")
        }
        if (komentarTerbanyak.isEmpty()) {
            Log.d("HomePopulerFragment", "Memuat komentar terbanyak")
            komentarTerbanyakViewModel.loadKomentarTerbanyak()
        }
    }

    private fun refreshContent() {
            beritaPopulerViewModel.loadBeritaPopuler()
            tagPopulerViewModel.loadTagPopuler()
            beritaRekomendasiViewModel.loadBeritaRekomendasi(  userId ?: "")
            komentarTerbanyakViewModel.loadKomentarTerbanyak()
            // Hentikan animasi refresh setelah data dimuat
            binding.swipeRefreshLayout.isRefreshing = false
        }


    private fun observeViewModel() {
        beritaPopulerViewModel.isLoading.observe(viewLifecycleOwner) { showShimmerEffect(it) }

        bookmarkViewModel.bookmarkStatusMap.observe(viewLifecycleOwner, Observer { statusMap ->
            // Perbarui status bookmark pada adapter jika status map diubah secara global
            beritaPopulerAdapter.updateBookmarkStatusMap(statusMap)
            beritaRekomendasiAdapter.updateBookmarkStatusMap(statusMap)
        })

        komentarTerbanyakViewModel.komentarTerbanyakList.observe(viewLifecycleOwner) {
            komentarTerbanyakList ->
            if (komentarTerbanyakList.isNotEmpty()) {
                komentarTerbanyak = komentarTerbanyakList
                komentarTerbanyakAdapter.updateKomentarTerbanyakList(komentarTerbanyakList)
            } else {
                showToast("Tidak ada komentar terbanyak.")
            }
        }

        beritaPopulerViewModel.beritaPopulerList.observe(viewLifecycleOwner) { beritaPopulerList ->
            if (beritaPopulerList.isNotEmpty()) {
                beritaPopuler = beritaPopulerList
                beritaPopulerAdapter.updateBeritaPopulerList(beritaPopulerList)

                // Update global bookmark status hanya saat data dimuat pertama kali
                val semuaBerita = beritaPopuler + beritaRekomendasi
                bookmarkViewModel.checkBookmarkStatus(  userId ?: "", semuaBerita)
            } else {
                showToast("Tidak ada berita populer.")
            }
        }

        beritaRekomendasiViewModel.beritaRekomendasiList.observe(viewLifecycleOwner) {
            beritaRekomendasiList ->
            if (beritaRekomendasiList.isNotEmpty()) {
                beritaRekomendasi = beritaRekomendasiList
                beritaRekomendasiAdapter.updateBeritaRekomendaList(beritaRekomendasiList)

                // Update global bookmark status hanya saat data dimuat pertama kali
                val semuaBerita = beritaRekomendasi + beritaPopuler
                bookmarkViewModel.checkBookmarkStatus(  userId ?: "", semuaBerita)

            } else {
                showToast("Tidak ada berita rekomendasi.")
                    binding.cardView.visibility = View.GONE
                    binding.tvLabelDisarankan.visibility = View.GONE
                    binding.line2.visibility = View.GONE
            }
        }

        tagPopulerViewModel.tagPopulerList.observe(viewLifecycleOwner) { tagPopulerList ->
            if (tagPopulerList.isNotEmpty()) {
                tagPopuler = tagPopulerList
                tagPopulerAdapter.updateTagPopulerList(tagPopulerList)
            } else {
                showToast("Tidak ada tag populer.")
            }
        }
    }

    private fun setupRecyclerViews() {
        beritaPopulerAdapter = BeritaPopulerAdapter(emptyList(), object :
            BeritaPopulerAdapter.OnItemClickListener {
            override fun onArticlePopulerClick(beritaPopuler: ListBerita) {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(android.R.anim.slide_in_left)
                    .setExitAnim(android.R.anim.slide_out_right)
                    .build()
                val bundle = Bundle().apply {
                    putString("beritaId", beritaPopuler.idBerita) // Kirim ID Berita
                }

                // Log data yang dikirimkan dalam bundle
                Log.d("Navigasi", "Kirim beritaId: ${beritaPopuler.idBerita}, " +
                        "Kategori: ${beritaPopuler.kategori}")
                requireParentFragment().findNavController().navigate(
                    R.id.action_navigation_home_to_detailArtikelFragment, bundle,navOptions)
            }

            override fun onBookmarkBeritaPopulerClick(beritaPopuler: ListBerita) {
                toggleBookmarkLocally(beritaPopuler) } }, bookmarkViewModel,  userId ?: "")


        beritaRekomendasiAdapter = BeritaRekomendasiAdapter(emptyList(), object :
            BeritaRekomendasiAdapter.OnItemClickListener {

            override fun onArticleRekomendasiClick(beritaRekomendasi: ListBerita) {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(android.R.anim.slide_in_left)
                    .setExitAnim(android.R.anim.slide_out_right)
                    .build()
                val bundle = Bundle().apply {
                    putString("beritaId", beritaRekomendasi.idBerita) // Kirim ID Berita
                }
                // Log data yang dikirimkan dalam bundle
                Log.d("Navigasi", "Kirim beritaId: ${beritaRekomendasi.idBerita}, " +
                        "Kategori: ${beritaRekomendasi.kategori}")
                requireParentFragment().findNavController().navigate(
                    R.id.action_navigation_home_to_detailArtikelFragment, bundle,navOptions
                )
            }

            override fun onBookmarkBeritaRekomendasiClick(beritaRekomendasi: ListBerita) {
                toggleBookmarkLocally(beritaRekomendasi) } },bookmarkViewModel,   userId ?: "")

        tagPopulerAdapter = TagPopulerAdapter(emptyList(), object :
            TagPopulerAdapter.OnItemClickListener {
            override fun onTagPopulerClick(tagPopuler: Tag) {
                Toast.makeText(requireContext(), "Tag Clicked: ${tagPopuler.namaTag}"
                    , Toast.LENGTH_SHORT).show()
                searchViewModel.loadSearch(null,null,tagPopuler.namaTag?:"")
                findNavController().navigate(R.id.action_navigation_home_to_searchResultsFragment)
            }
        })

        komentarTerbanyakAdapter = KomentarTerbanyakAdapter(emptyList(), object :
            KomentarTerbanyakAdapter.OnItemClickListener {
            override fun onBeritaKomentarTerbanyakClick(komentarTerbanyak: ListBerita) {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(android.R.anim.slide_in_left)
                    .setExitAnim(android.R.anim.slide_out_right)
                    .build()
                val bundle = Bundle().apply {
                    putString("beritaId", komentarTerbanyak.idBerita) // Kirim ID Berita
                }
                // Log data yang dikirimkan dalam bundle
                Log.d("Navigasi", "Kirim beritaId: ${komentarTerbanyak.idBerita}")
                requireParentFragment().findNavController().navigate(
                    R.id.action_navigation_home_to_detailArtikelFragment, bundle, navOptions)
            }
        })


        binding.rvKomanterTerbanyak.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = komentarTerbanyakAdapter
        }

        binding.rvTagPopuler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tagPopulerAdapter
        }

        binding.rvBeritaTeratasPopuler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = beritaPopulerAdapter
        }

        binding.rvBeritaRekomendasi.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false)
            adapter = beritaRekomendasiAdapter
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
                delay(3000)
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

    private fun toggleBookmarkLocally(berita: ListBerita) {
        val beritaId = berita.idBerita ?: return
        val bookmark = ResultBookmark(  userId ?: "", beritaId)

        // Perbarui status lokal pada adapter
        beritaPopulerAdapter.toggleBookmarkStatus(beritaId)
        beritaRekomendasiAdapter.toggleBookmarkStatus(beritaId)

        // Operasi toggle bookmark secara global
        bookmarkViewModel.toggleBookmark(bookmark, requireContext())
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
                rvKomanterTerbanyak.visibility = View.GONE
                btnNext.visibility = View.GONE
                btnPrev.visibility = View.GONE
                line1.visibility = View.GONE
                line2.visibility = View.GONE
                line3.visibility = View.GONE
                tvLabelTerpopuler.visibility = View.GONE
                tvLabelDisarankan.visibility = View.GONE
                komentarTerbanyak.visibility = View.GONE
            } else {
                shimmerBeritaPopuler.stopShimmer()
                shimmerBeritaPopuler.visibility = View.GONE
                rvBeritaTeratasPopuler.visibility = View.VISIBLE
                tvLabelTerpopuler.visibility = View.VISIBLE
                rvTagPopuler.visibility = View.VISIBLE
                rvBeritaRekomendasi.visibility = View.VISIBLE
                rvKomanterTerbanyak.visibility = View.VISIBLE
                btnNext.visibility = View.VISIBLE
                btnPrev.visibility = View.VISIBLE
                line1.visibility = View.VISIBLE
                line2.visibility = View.VISIBLE
                line3.visibility = View.VISIBLE
                tvLabelTerpopuler.visibility = View.VISIBLE
                tvLabelDisarankan.visibility = View.VISIBLE
                komentarTerbanyak.visibility = View.VISIBLE
            }
        }
        // Periksa apakah userId ada
        if (userId == null) {
            // Jika userId tidak ditemukan (belum login), sembunyikan elemen-elemen terkait
            Log.d("HomePopulerFragment", "User belum login, sembunyikan elemen-elemen terkait")
            binding.cardView.visibility = View.GONE
            binding.tvLabelDisarankan.visibility = View.GONE
            binding.line2.visibility = View.GONE
        }
//        } else {
//            // Jika userId ada (sudah login), tampilkan elemen-elemen tersebut
//            Log.d("HomePopulerFragment", "User teridentifikasi: $userId")
//            binding.cardView.visibility = View.VISIBLE
//            binding.tvLabelDisarankan.visibility = View.VISIBLE
//            binding.line2.visibility = View.VISIBLE
//        }
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