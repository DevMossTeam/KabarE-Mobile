package com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.databinding.FragmentDalamPeninjauanBinding
import com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis.artikeladapters.DalamPeninjauanAdapter
import com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis.artikelviewmodels.DalamPeninjauanViewModel

class DalamPeninjauanFragment : Fragment(), DalamPeninjauanAdapter.OnItemClickListener {
    private var _binding: FragmentDalamPeninjauanBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DalamPeninjauanViewModel by viewModels()
    private lateinit var adapter: DalamPeninjauanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDalamPeninjauanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = DalamPeninjauanAdapter(emptyList(), this) // Menggunakan 'this' sebagai listener
        binding.rvDalamPeninjauan.layoutManager = LinearLayoutManager(context)
        binding.rvDalamPeninjauan.adapter = adapter
    }

    private fun observeData() {
        // Di sini Anda bisa mengamati data dari ViewModel dan memperbarui adapter
        // Misalnya:
        viewModel.dalamPeninjauanLiveData.observe(viewLifecycleOwner) { dataList ->
            adapter = DalamPeninjauanAdapter(dataList, this) // Update adapter dengan data baru
            binding.rvDalamPeninjauan.adapter = adapter
        }
    }

    // Implementasi metode dari OnItemClickListener
    override fun onBatalPengajuanClick(position: Int) {
        // Tangani klik untuk membatalkan pengajuan
        val clickedItem = viewModel.dalamPeninjauanLiveData.value?.get(position)
        clickedItem?.let {
            // Logika untuk membatalkan pengajuan
        }
    }

    override fun onPindahKeDrafClick(position: Int) {
        // Tangani klik untuk memindahkan ke draf
        val clickedItem = viewModel.dalamPeninjauanLiveData.value?.get(position)
        clickedItem?.let {
            // Logika untuk memindahkan item ke draf
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
