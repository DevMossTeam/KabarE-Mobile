package com.devmoss.kabare.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.ui.home.homeviewmodels.HomeTerbaruViewModel
import com.devmoss.kabare.databinding.FragmentHomeTerbaruBinding
import com.devmoss.kabare.ui.home.homeadapters.BeritaTerkiniAdapater
import com.devmoss.kabare.ui.home.homeadapters.BeritaTeratasAdapter

class HomeTerbaruFragment : Fragment() {

    private var _binding: FragmentHomeTerbaruBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeTerbaruViewModel by viewModels()
    private lateinit var beritaTerkiniAdapter: BeritaTerkiniAdapater
    private lateinit var beritaTeratasAdapter: BeritaTeratasAdapter

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
        observeData()
        viewModel.loadArticles() // Memuat artikel setelah tampilan siap
    }

    private fun setupRecyclerViews() {
        // Inisialisasi adapter untuk berita terkini
        beritaTerkiniAdapter = BeritaTerkiniAdapater(emptyList(), object : BeritaTerkiniAdapater.OnItemClickListener {

            override fun onArticleTerkiniClick(article: Artikel) {
                val bundleBeritaTerkini = Bundle().apply {
                    putParcelable("articleTerkini", article) // Kirim seluruh objek artikel
                }

                // Navigasi ke DetailArtikelFragment
                findNavController().navigate(R.id.action_navigation_home_to_detailArtikelFragment, bundleBeritaTerkini)
            }
            override fun onBookmarkBeritaTerkiniClick(article: Artikel) {
                viewModel.toggleBookmark(article) // Update bookmark status
            }
        })
        // Set up RecyclerView untuk berita terkini
        binding.rvBeritaTerkini.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = beritaTerkiniAdapter
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Inisialisasi adapter untuk berita teratas
        beritaTeratasAdapter = BeritaTeratasAdapter(emptyList(), object : BeritaTeratasAdapter.OnItemClickListener {
            override fun onBookmarkBeritaTeratasClick(article: Artikel) {

                viewModel.toggleBookmark(article) // Update bookmark status
            }
            override fun onArticleTeratasClick(article: Artikel) {
                val bundleBeritaTeratas = Bundle().apply {
                    putParcelable("articleTeratas", article) // Kirim seluruh objek artikel
                }
                // Navigasi ke DetailArtikelFragment
                findNavController().navigate(R.id.action_navigation_home_to_detailArtikelFragment, bundleBeritaTeratas)

            }
        })
        // Set up RecyclerView untuk berita teratas
        binding.rvBeritaTeratas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = beritaTeratasAdapter
        }
    }

    private fun observeData() {
        viewModel.sortedArticles.observe(viewLifecycleOwner) { articles ->
            // Pisahkan artikel menjadi berita terkini dan berita teratas
            val beritaTerkini = articles.filter { it.isTerkini } // Sesuaikan dengan logika bisnis Anda
            val beritaTeratas = articles.filter { it.isTeratas } // Sesuaikan dengan logika bisnis Anda

            // Perbarui data di adapter
            beritaTerkiniAdapter.updateList(beritaTerkini)
            beritaTeratasAdapter.updateList(beritaTeratas)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Hapus referensi binding untuk mencegah memory leak
    }
}
