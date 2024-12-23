package com.devmoss.kabare.ui.notifikasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.FragmentNotifikasiBinding
import com.devmoss.kabare.ui.notifikasi.notifikasiadapters.NotifikasiAdapter
import com.devmoss.kabare.ui.home.terbaru.terbaruviewmodels.BeritaTerkiniViewModel
import java.util.*

class NotifikasiFragment : Fragment(), NotifikasiAdapter.OnItemClickListener {

    private lateinit var binding: FragmentNotifikasiBinding
    private lateinit var notifikasiAdapter: NotifikasiAdapter
    private val beritaTerkiniViewModel: BeritaTerkiniViewModel by activityViewModels()

    private var notifikasiList: List<ListBerita> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotifikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupRecyclerView()
    }

    private fun observeViewModel() {
        beritaTerkiniViewModel.beritaTerkiniList.observe(viewLifecycleOwner, Observer { beritaList ->
            // Filter berita dalam 24 jam terakhir
            val recentBeritaList = beritaList.filter { isWithinLast24Hours(it.tanggalDiterbitkan) }

            if (recentBeritaList.isNotEmpty()) {
                notifikasiList = recentBeritaList
                notifikasiAdapter.updateNotifikasiList(notifikasiList)
            } else {
                Toast.makeText(requireContext(), "Belum ada notifikasi dalam 24 jam terakhir!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        notifikasiAdapter = NotifikasiAdapter(emptyList(), this)

        binding.rvNotifikasi.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = notifikasiAdapter
        }
    }

    override fun onJudulArtikelClick(beritaTerkini: ListBerita) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(android.R.anim.slide_in_left)
            .setExitAnim(android.R.anim.slide_out_right)
            .build()

        val bundle = Bundle().apply {
            putString("beritaId", beritaTerkini.idBerita) // Kirim ID Berita
        }

        findNavController().navigate(
            R.id.action_navigation_notifications_to_detailArtikelFragment, bundle, navOptions
        )
    }

    // Fungsi untuk memeriksa apakah timestamp berada dalam 24 jam terakhir
    private fun isWithinLast24Hours(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis() // Waktu saat ini dalam milidetik
        val diff = currentTime - timestamp
        return diff <= 24 * 60 * 60 * 1000 // 24 jam dalam milidetik
    }
}
