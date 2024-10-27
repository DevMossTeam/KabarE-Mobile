package com.devmoss.kabare.ui.notifikasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.databinding.FragmentNotifikasiBinding
import com.devmoss.kabare.ui.notifikasi.notifikasiadapters.NotifikasiAdapter
import com.devmoss.kabare.ui.notifikasi.notifikasiviewmodels.NotificationViewModel

class NotifikasiFragment : Fragment(), NotifikasiAdapter.OnItemClickListener {

    private lateinit var binding: FragmentNotifikasiBinding
    private lateinit var notifikasiAdapter: NotifikasiAdapter
    private val viewModel: NotificationViewModel by viewModels() // Mendapatkan instance ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotifikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadData()
    }

    private fun setupRecyclerView() {
        // Inisialisasi adapter dengan daftar kosong
        notifikasiAdapter = NotifikasiAdapter(emptyList(), this)
        binding.rvNotifikasi.layoutManager = LinearLayoutManager(context)
        binding.rvNotifikasi.adapter = notifikasiAdapter
    }

    private fun loadData() {
        // Mengamati LiveData dari ViewModel
        viewModel.notificationsLiveData.observe(viewLifecycleOwner) { notifications ->
            notifikasiAdapter = NotifikasiAdapter(notifications, this) // Mengganti adapter dengan data baru
            binding.rvNotifikasi.adapter = notifikasiAdapter
        }
    }

    // Implementasi dari OnItemClickListener
    override fun onJudulArtikelClick(position: Int) {
        // Tangani klik pada judul artikel
    }
}
