package com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.databinding.FragmentPublishedArtikelBinding
import com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis.artikeladapters.PublishedArtikelAdapter
import com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis.artikelviewmodels.PublishedArtikelViewModel

class PublishedArtikelFragment : Fragment(), PublishedArtikelAdapter.OnItemClickListener {
    private var _binding: FragmentPublishedArtikelBinding? = null
    private val binding get() = _binding!!
    private val viewModel : PublishedArtikelViewModel by viewModels()
    private lateinit var adapter: PublishedArtikelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublishedArtikelBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
    }


    // Setup RecyclerView dengan adapter
    private fun setupRecyclerView() {
        adapter = PublishedArtikelAdapter(emptyList(), this) // Mengirimkan listener (this) ke adapter
        binding.rvPublishedArtikel.adapter = adapter
        binding.rvPublishedArtikel.layoutManager = LinearLayoutManager(context)
    }

    // Observasi data dari ViewModel
    private fun observeData() {
        viewModel.publishedArtikelLiveData.observe(viewLifecycleOwner) { artikelList ->
            adapter.updateData(artikelList)
        }
    }

    // Implementasi listener saat item judul diklik
    override fun onJudulClick(position: Int) {
        val artikel = viewModel.publishedArtikelLiveData.value?.get(position)
        artikel?.let {
            // TODO: Buka halaman detail artikel
            // Anda bisa menggunakan Navigation component atau Intent untuk membuka detail artikel
        }
    }

    // Implementasi listener saat menu edit/delete di klik
    override fun onMenuClick(position: Int, action: String) {
        val artikel = viewModel.publishedArtikelLiveData.value?.get(position)
        artikel?.let {
            when (action) {
                "Unpublish" -> {
                    // TODO: Tambahkan logika untuk membuka halaman edit artikel
                    // Misal, Anda bisa gunakan Navigation component atau Intent
                }
                "Hapus" -> {
                    // TODO: Tambahkan logika untuk menghapus artikel
                    // Misal, menghapus data dari ViewModel atau database
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Menghindari memory leaks
    }
}
