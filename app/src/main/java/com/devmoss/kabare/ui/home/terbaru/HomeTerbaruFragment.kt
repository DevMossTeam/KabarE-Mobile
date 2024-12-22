package com.devmoss.kabare.ui.home.terbaru

import android.os.Bundle
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
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResultBookmark
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.databinding.FragmentHomeTerbaruBinding
import com.devmoss.kabare.ui.reaksi.reaksiviewmodels.BookmarkViewModel
import com.devmoss.kabare.ui.home.detailberita.detailberitaadapters.BeritaTerkaitAdapter
import com.devmoss.kabare.ui.home.terbaru.terbaruadapters.BeritaTerkiniAdapter
import com.devmoss.kabare.ui.home.terbaru.terbaruviewmodels.BeritaTerkiniViewModel

class HomeTerbaruFragment : Fragment() {

    private var _binding: FragmentHomeTerbaruBinding? = null
    private val binding get() = _binding!!
    private var scrollPositionY: Int = 0
    private lateinit var beritaTerkiniAdapter: BeritaTerkiniAdapter
    private lateinit var beritaTerkaitAdapter: BeritaTerkaitAdapter
    private val beritaTerkiniViewModel: BeritaTerkiniViewModel by activityViewModels()
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()

    private var beritaTerkiniList: List<ListBerita> = emptyList()

    // inisialisai user repo
    private lateinit var userRepository: UserRepository
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeTerbaruBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()

        // Setup SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshContent()
        }

        // Inisialisasi UserRepository
        userRepository = UserRepository(requireContext())
        userId = userRepository.getUserUid() ?: run {
//            Toast.makeText(requireContext(), "User belum login!", Toast.LENGTH_SHORT).show()
            return // Jika userId null, hentikan proses lebih lanjut
        }
    }

    override fun onResume() {
        super.onResume()
        if (beritaTerkiniList.isEmpty()) {
            beritaTerkiniViewModel.loadBeritaTerkini()
        }
    }

    override fun onPause() {
        super.onPause()
        // Simpan posisi scroll
        scrollPositionY = binding.nestedScrollView.scrollY
    }

    private fun refreshContent() {
        beritaTerkiniViewModel.loadBeritaTerkini() // Muat ulang berita
        binding.swipeRefreshLayout.isRefreshing = false // Hentikan indikator refresh
    }

    private fun observeViewModel() {

        beritaTerkiniViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showShimmerEffect(isLoading)
        })

        bookmarkViewModel.bookmarkStatusMap.observe(viewLifecycleOwner, Observer { statusMap ->
            // Perbarui status bookmark pada adapter jika status map diubah secara global
            beritaTerkiniAdapter.updateBookmarkStatusMap(statusMap)
            beritaTerkaitAdapter.updateBookmarkStatusMap(statusMap)
        })

        beritaTerkiniViewModel.beritaTerkiniList.observe(viewLifecycleOwner, Observer { beritaList ->
            if (beritaList.isNotEmpty()) {
                // Ambil hanya satu berita untuk berita terkini
                val beritaTerkiniSingle = listOf(beritaList.first())
                beritaTerkiniList = beritaTerkiniSingle
                beritaTerkiniAdapter.updateList(beritaTerkiniSingle)

                // Tetap menyimpan semua berita untuk keperluan global
                beritaTerkiniList = beritaList

                // Hapus berita pertama dari daftar untuk berita terkait
                val beritaTerkaitList = beritaList.drop(1) // Menghapus elemen pertama
                beritaTerkaitAdapter.updateList(beritaTerkaitList)

                val semuaBerita = beritaTerkiniSingle + beritaTerkaitList
                // Update global bookmark status hanya saat data dimuat pertama kali
                bookmarkViewModel.checkBookmarkStatus( userId ?: "",semuaBerita)
            } else {
                Toast.makeText(requireContext(), "Tidak ada berita terbaru."
                    ,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerViews() {
        beritaTerkiniAdapter = BeritaTerkiniAdapter(emptyList(),
            object : BeritaTerkiniAdapter.OnItemClickListener {
                override fun onArticleTerkiniClick(beritaTerkini: ListBerita) {
                    val navOptions = NavOptions.Builder()
                        .setEnterAnim(android.R.anim.slide_in_left)
                        .setExitAnim(android.R.anim.slide_out_right)
                        .build()
                    val bundle = Bundle().apply {
                        putString("beritaId", beritaTerkini.idBerita) // Kirim ID Berita
                    }
                    requireParentFragment().findNavController().navigate(
                        R.id.action_navigation_home_to_detailArtikelFragment, bundle,navOptions)
                }

                override fun onBookmarkBeritaTerkiniClick(beritaTerkini: ListBerita) {
                    if (userId == null) {
                        Toast.makeText(requireContext(), "Silahkan login terlebih dahulu!", Toast.LENGTH_SHORT).show()
                        return
                    }else {
                        toggleBookmarkLocally(beritaTerkini)
                    }
                }
            },
            bookmarkViewModel,
            userId ?: ""
        )

        binding.rvBeritaTerkini.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = beritaTerkiniAdapter
        }

        beritaTerkaitAdapter = BeritaTerkaitAdapter(emptyList(),
            object : BeritaTerkaitAdapter.OnItemClickListener {
                override fun onArticleTerkaitClick(beritaTerkait: ListBerita) {
                    val navOptions = NavOptions.Builder()
                        .setEnterAnim(android.R.anim.slide_in_left)
                        .setExitAnim(android.R.anim.slide_out_right)
                        .build()
                    val bundle = Bundle().apply {
                        putString("beritaId", beritaTerkait.idBerita) // Kirim ID Berita
                    }
                    requireParentFragment().findNavController().navigate(
                        R.id.action_navigation_home_to_detailArtikelFragment, bundle,navOptions)
                }

                override fun onBookmarkBeritaTerkaitClick(beritaTerkait: ListBerita) {
                    if (userId == null) {
                        Toast.makeText(requireContext(), "Silahkan login terlebih dahulu!", Toast.LENGTH_SHORT).show()
                        return
                    }else {
                        toggleBookmarkLocally(beritaTerkait)
                    }

                }
            },
            bookmarkViewModel,
            userId ?: ""
        )

        binding.rvBeritaTerkait.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = beritaTerkaitAdapter
        }
    }

    private fun toggleBookmarkLocally(berita: ListBerita) {
        val beritaId = berita.idBerita ?: return
        val bookmark = ResultBookmark( userId ?: "", beritaId)

        // Perbarui status lokal pada adapter
        beritaTerkiniAdapter.toggleBookmarkStatus(beritaId)
        beritaTerkaitAdapter.toggleBookmarkStatus(beritaId)

        // Operasi toggle bookmark secara global
        bookmarkViewModel.toggleBookmark(bookmark, requireContext())

    }
    private fun showShimmerEffect(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerBeritaTerkini.startShimmer()
            binding.shimmerBeritaTerkini.visibility = View.VISIBLE
            binding.rvBeritaTerkini.visibility = View.GONE
            binding.rvBeritaTerkait.visibility = View.GONE
            binding.tvBeritaTerkini.visibility = View.GONE
            binding.tvBeritaTeratas.visibility = View.GONE
            binding.lineTeratas.visibility = View.GONE
            binding.tvBeritaTeratas.visibility = View.GONE
            binding.akhirBerita.visibility = View.GONE
        } else {
            binding.shimmerBeritaTerkini.stopShimmer()
            binding.shimmerBeritaTerkini.visibility = View.GONE
            binding.rvBeritaTerkini.visibility = View.VISIBLE
            binding.rvBeritaTerkait.visibility = View.VISIBLE
            binding.tvBeritaTerkini.visibility = View.GONE
            binding.tvBeritaTeratas.visibility = View.GONE
            binding.lineTeratas.visibility = View.GONE
        binding.akhirBerita.visibility = View.VISIBLE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

