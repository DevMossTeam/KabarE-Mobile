package com.devmoss.kabare.ui.komentarartikel

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.databinding.FragmentKomentarDialogBinding
import com.devmoss.kabare.ui.komentarartikel.komentaradapter.KomentarArtikelAdapter
import com.devmoss.kabare.ui.komentarartikel.komentarviewmodel.KomentarViewModel
import com.devmoss.kabare.ui.report.ReportDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@Suppress("UNREACHABLE_CODE")
class KomentarDialogFragment : BottomSheetDialogFragment(), KomentarArtikelAdapter.OnItemClickListener {

    private var jumlahKomentar: Int = 0

    private var idBerita: String? = null
    private var _binding: FragmentKomentarDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: KomentarViewModel by viewModels()
    private lateinit var adapter: KomentarArtikelAdapter

    // inisialisai user repo
    private lateinit var userRepository: UserRepository
    private var userId: String? = null


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

        // Inisialisasi UserRepository
        userRepository = UserRepository(requireContext())
        userId = userRepository.getUserUid() ?: run {
            Toast.makeText(requireContext(), "User belum login!", Toast.LENGTH_SHORT).show()
            return
        }

        idBerita = arguments?.getString("BERITA_ID")
        jumlahKomentar = arguments?.getInt("JUMLAH_KOMENTAR") ?: 0
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
                viewModel.postKomentar(userId?: "", idBerita!!, teksKomentar)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = KomentarArtikelAdapter(emptyList(),this,userId = userId?: "")
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

        // tampilkan logo vektor ketika tidak ada komentar
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
            binding.jumlahKomen.text =  "${komentarList.size}"
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
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menghapus komentar ini?")
                .setPositiveButton("Ya") { _, _ ->
                    viewModel.deleteKomentar(id)
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
        } else {
            Log.e("KomentarDialogFragment", "ID komentar tidak valid!")
        }
    }

    override fun onReportKomentarClick(komentarId: String, ownerId: String) {
        if (ownerId == userId) {
            Toast.makeText(requireContext(), "Anda tidak dapat melaporkan komentar Anda sendiri.", Toast.LENGTH_SHORT).show()
        } else {
            // Proses melaporkan komentar
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Laporkan Komentar")
                .setMessage("Apakah Anda yakin ingin melaporkan komentar ini?")
                .setPositiveButton("Ya") { _, _ ->
                    val dialogLaporan = ReportDialogFragment.newInstance(idBerita,komentarId)
                    dialogLaporan.show(childFragmentManager, "ReportDialogFragment")
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
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