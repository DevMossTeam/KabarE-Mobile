package com.devmoss.kabare.ui.komentarartikel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.databinding.FragmentKomentarDialogBinding
import com.devmoss.kabare.ui.komentarartikel.komentaradapter.KomentarArtikelAdapter
import com.devmoss.kabare.ui.komentarartikel.komentarviewmodel.KomentarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class KomentarDialogFragment : BottomSheetDialogFragment(), KomentarArtikelAdapter.OnItemClickListener {

    private var jumlahKomentar: Int = 0

    private var idBerita: String? = null
    private var _binding: FragmentKomentarDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: KomentarViewModel by viewModels()
    private lateinit var adapter: KomentarArtikelAdapter

    companion object {
        fun newInstance(idBerita: String,jumlahKomentar : Int): KomentarDialogFragment {
            val fragment = KomentarDialogFragment()
            val args = Bundle().apply {
                putString("BERITA_ID", idBerita)
                putInt("JUMLAH_KOMENTAR", jumlahKomentar)

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
        jumlahKomentar = arguments?.getInt("JUMLAH_KOMENTAR") ?: 0 // Ambil jumlah komentar dari argumen
        if (idBerita.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "ID Berita tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }
        // Menampilkan jumlah komentar yang diambil dari argumen
        binding.jumlahKomen.text = "$jumlahKomentar"

        setupRecyclerView()
        observeData()
        viewModel.getKomentar(idBerita!!) // Muat data komentar

        binding.btnSend.setOnClickListener {
            val teksKomentar = binding.etComment.text.toString().trim()
            if (teksKomentar.isEmpty()) {
                Toast.makeText(requireContext(), "Komentar tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                val userId = "3" // Ganti dengan user ID yang valid
                viewModel.postKomentar(userId, idBerita!!, teksKomentar)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = KomentarArtikelAdapter(emptyList(),this,userId = "3")
        binding.rvKomentar.layoutManager = LinearLayoutManager(context)
        binding.rvKomentar.adapter = adapter
    }

    private fun observeData() {
        // Shimmer untuk loading pertama kali
        viewModel.isInitialLoad.observe(viewLifecycleOwner) { isInitialLoad ->
            if (isInitialLoad) {
                showShimmerEffect(true)
            } else {
                showShimmerEffect(false)
            }
        }

        // Observasi komentar
        viewModel.komentarList.observe(viewLifecycleOwner) { komentarList ->
            if (komentarList.isNullOrEmpty()) {
                binding.vectorKomentar.visibility = View.VISIBLE
                binding.rvKomentar.visibility = View.GONE
            } else {
                binding.vectorKomentar.visibility = View.GONE
                binding.rvKomentar.visibility = View.VISIBLE
                adapter.updateList(komentarList)
            }
            // Update jumlah komentar (jika ada komentar baru)
            binding.jumlahKomen.text = "${komentarList.size}"

        }

        // Status tambah komentar
        viewModel.statusKomentar.observe(viewLifecycleOwner) { status ->
            if (status == "success") {
                Toast.makeText(requireContext(), "Komentar berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                binding.etComment.text.clear()
                viewModel.getKomentar(idBerita!!, isInitialLoad = false) // Refresh tanpa shimmer
            } else if (status == "error") {
                Toast.makeText(requireContext(), "Gagal menambahkan komentar", Toast.LENGTH_SHORT).show()
            }
        }

        // Status hapus komentar
        viewModel.deleteKomentarStatus.observe(viewLifecycleOwner) { status ->
            if (status == "success") {
                Toast.makeText(requireContext(), "Komentar berhasil dihapus!", Toast.LENGTH_SHORT).show()
                viewModel.getKomentar(idBerita!!, isInitialLoad = false) // Refresh tanpa shimmer
            } else if (status == "error") {
                Toast.makeText(requireContext(), "Gagal menghapus komentar", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onHapusKOmentarClick(id: String) {
        if (id.isNotEmpty()) {
            viewModel.deleteKomentar(id) // Menghapus komentar
        } else {
            Log.e("KomentarDialogFragment", "ID komentar tidak valid!")
        }
    }


    private fun showShimmerEffect(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerKomentar.startShimmer()
            binding.shimmerKomentar.visibility = View.VISIBLE
            binding.rvKomentar.visibility = View.GONE
            binding.line.visibility = View.GONE
        } else {
            binding.shimmerKomentar.stopShimmer()
            binding.shimmerKomentar.visibility = View.GONE
            binding.rvKomentar.visibility = View.VISIBLE
            binding.line.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
