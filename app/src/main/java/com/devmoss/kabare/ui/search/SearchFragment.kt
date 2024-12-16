package com.devmoss.kabare.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupSearch()
        setupCategoryClickListeners()

        return binding.root
    }

    private fun setupSearch() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val inputKatakunci = binding.etSearch.text.toString().trim()
                if (inputKatakunci.isNotEmpty()) {
                    performSearch(katakunci = inputKatakunci)
                } else {
                    Toast.makeText(context, "Kata kunci pencarian tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
    }

    private fun setupCategoryClickListeners() {
        binding.apply {
            kampus.setOnClickListener { performSearch(kategori = "Kampus") }
            prestasi.setOnClickListener { performSearch(kategori = "Prestasi") }
            politik.setOnClickListener { performSearch(kategori = "Politik") }
            kesehatan.setOnClickListener { performSearch(kategori = "Kesehatan") }
            olahraga.setOnClickListener { performSearch(kategori = "Olahraga") }
            ekonomi.setOnClickListener { performSearch(kategori = "Ekonomi") }
            bisnis.setOnClickListener { performSearch(kategori = "Bisnis") }
            ukm.setOnClickListener { performSearch(kategori = "UKM") }
            beritaLainnya.setOnClickListener { performSearch(kategori = "Berita Lainnya") }
        }
    }

    private fun performSearch(katakunci: String? = null, kategori: String? = null) {
        if (katakunci.isNullOrEmpty() && kategori.isNullOrEmpty()) {
            Toast.makeText(context, "Harap masukkan kata kunci atau pilih kategori untuk pencarian", Toast.LENGTH_SHORT).show()
            return
        }
        // Panggil ViewModel untuk memuat pencarian dengan parameter yang sesuai
        searchViewModel.loadSearch(katakunci = katakunci, kategori = kategori)
        findNavController().navigate(R.id.action_navigation_cari_to_searchResultsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
