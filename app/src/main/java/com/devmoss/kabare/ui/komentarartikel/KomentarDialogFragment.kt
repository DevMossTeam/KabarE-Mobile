package com.devmoss.kabare.ui.komentarartikel

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.data.model.KomentarArtikel
import com.devmoss.kabare.data.repository.KomentarRepository
import com.devmoss.kabare.databinding.FragmentKomentarDialogBinding
import com.devmoss.kabare.ui.komentarartikel.komentaradapter.KomentarArtikelAdapter
import com.devmoss.kabare.ui.komentarartikel.komentarviewmodel.KomentarArtikelViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class KomentarDialogFragment : BottomSheetDialogFragment(), KomentarArtikelAdapter.OnItemClickListener {

    private var idBerita: String? = null
    private var _binding: FragmentKomentarDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: KomentarArtikelViewModel by viewModels()
    private lateinit var adapter: KomentarArtikelAdapter

    companion object {
        fun newInstance(id: String): KomentarDialogFragment {
            val fragment = KomentarDialogFragment()
            val args = Bundle().apply {
                putString("BERITA_ID", id) // Simpan id yang diterima
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKomentarDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idBerita = arguments?.getString("BERITA_ID")
        setupRecyclerView()
        observeData()

        // Muat komentar untuk artikel tertentu

        // Mengonversi idBerita dari String ke Int sebelum dipanggil
        idBerita?.toIntOrNull()?.let { viewModel.loadComments(it) } // Pastikan idBerita adalah angka yang valid
    }

    private fun setupRecyclerView() {
        // Inisialisasi adapter dengan list kosong
        adapter = KomentarArtikelAdapter(emptyList(), this)
        binding.rvKomentar.layoutManager = LinearLayoutManager(context)
        binding.rvKomentar.adapter = adapter
    }

    private fun observeData() {
        // Observasi LiveData dari ViewModel untuk daftar komentar
        viewModel.comments.observe(viewLifecycleOwner) { komentarList ->
            if (komentarList.isNullOrEmpty()) {
                // Tampilkan indikator jika tidak ada komentar
                binding.vectorKomentar.visibility = View.VISIBLE
                binding.rvKomentar.visibility = View.GONE
            } else {
                // Sembunyikan indikator dan tampilkan RecyclerView
                binding.vectorKomentar.visibility = View.GONE
                binding.rvKomentar.visibility = View.VISIBLE
                adapter.updateList(komentarList)
            }
        }
    }

    // Tindakan ketika item komentar diklik untuk dihapus
    override fun onHapusKOmentarClick(komentar: KomentarArtikel) {
        // Tampilkan popup konfirmasi
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus komentar ini?")
            .setPositiveButton("Hapus") { _, _ ->
                // Hapus komentar jika dikonfirmasi
                viewModel.hapusKomentar(komentar) // Pastikan metode ini ada di ViewModel
                Toast.makeText(requireContext(), "Komentar dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Menghapus referensi binding untuk mencegah memory leak
    }
}
