package com.devmoss.kabare.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.FragmentSearchResultsBinding
import com.devmoss.kabare.ui.reaksi.reaksiadapters.DaftarBeritaBookmarkAdapter
import com.devmoss.kabare.ui.reaksi.reaksiviewmodels.BookmarkViewModel

class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    private lateinit var beritaAdapter: DaftarBeritaBookmarkAdapter
    private var allResults: List<ListBerita> = emptyList() // Semua hasil tanpa filter

    val userId = "2"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)

        // Ubah warna icon back ke white
        binding.back.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))

        // Set listener untuk tombol back
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        setupRecyclerView()
        observeSearchResults()
        setupTimeFilter() // Tambahkan untuk mengatur filter waktu

        return binding.root
    }

    private fun setupRecyclerView() {
        beritaAdapter = DaftarBeritaBookmarkAdapter(emptyList(), object : DaftarBeritaBookmarkAdapter.OnItemClickListener {
            override fun onBeritaBookmarkClick(beritaBookmark: ListBerita) {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(android.R.anim.slide_in_left)
                    .setExitAnim(android.R.anim.slide_out_right)
                    .build()
                val bundle = Bundle().apply {
                    putString("beritaId", beritaBookmark.idBerita) // Kirim ID Berita
                }
                requireParentFragment().findNavController().navigate(R.id.action_searchResultsFragment_to_detailArtikelFragment, bundle, navOptions)
            }
        })

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = beritaAdapter
        }
    }

    private fun observeSearchResults() {
        searchViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar(isLoading)
        })

        searchViewModel.searchList.observe(viewLifecycleOwner) { listBerita ->
            binding.progresBar.visibility = View.GONE
            if (!listBerita.isNullOrEmpty()) {
                allResults = listBerita // Simpan semua hasil
                beritaAdapter.updateList(listBerita) // Tampilkan tanpa filter
                binding.rvSearchResults.visibility = View.VISIBLE
                binding.searchEmpty.visibility = View.GONE
            } else {
                binding.rvSearchResults.visibility = View.GONE
                binding.searchEmpty.visibility = View.VISIBLE
            }
        }
    }

//    private fun setupTimeFilter() {
//        // Listener untuk filter waktu di Spinner
//        binding.spinnerTimeFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val filteredResults = filterResultsByTime(allResults, position)
//                beritaAdapter.updateList(filteredResults)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
//    }

    private fun setupTimeFilter() {
        // Listener untuk filter waktu di Spinner
        binding.spinnerTimeFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val filteredResults = filterResultsByTime(allResults, position)

                if (filteredResults.isEmpty()) {
                    binding.rvSearchResults.visibility = View.GONE
                    binding.searchEmpty.visibility = View.VISIBLE
                } else {
                    beritaAdapter.updateList(filteredResults)
                    binding.rvSearchResults.visibility = View.VISIBLE
                    binding.searchEmpty.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    private fun filterResultsByTime(results: List<ListBerita>, filterOption: Int): List<ListBerita> {
        val currentTime = System.currentTimeMillis()

        return when (filterOption) {
            1 -> { // Hari Ini
                results.filter {
                    val articleTime = it.tanggalDiterbitkan
                    isWithinTimeRange(currentTime, articleTime, 1)
                }
            }
            2 -> { // 7 Hari Terakhir
                results.filter {
                    val articleTime = it.tanggalDiterbitkan
                    isWithinTimeRange(currentTime, articleTime, 7)
                }
            }
            else -> results // Semua (Jika tidak ada filter yang dipilih)
        }

    }

    private fun isWithinTimeRange(currentTime: Long, articleTime: Long, days: Int): Boolean {
        val daysInMillis = days * 24 * 60 * 60 * 1000L
        return (currentTime - articleTime) <= daysInMillis
    }

    private fun progressBar(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progresBar.visibility = View.VISIBLE
            } else {
                progresBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
