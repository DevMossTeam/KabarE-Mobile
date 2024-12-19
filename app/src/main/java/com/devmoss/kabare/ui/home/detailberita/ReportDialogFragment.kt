package com.devmoss.kabare.ui.home.detailberita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.devmoss.kabare.R
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.databinding.FragmentReportDialogBinding
import com.devmoss.kabare.ui.home.detailberita.detailviewmodels.ReportBeritaViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReportDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentReportDialogBinding? = null
    private val binding get() = _binding!!

    // inisialisasi user repo
    private lateinit var userRepository: UserRepository
    private var userId: String? = null

    // ViewModel untuk Report Berita
    private val reportBeritaViewModel: ReportBeritaViewModel by viewModels()

    companion object {
        private const val ARG_ID_BERITA = "id_berita"

        // Fungsi untuk membuat instance baru dari fragment dengan argumen
        fun newInstance(idBerita: String): ReportDialogFragment {
            val fragment = ReportDialogFragment()
            val args = Bundle()
            args.putString(ARG_ID_BERITA, idBerita)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Data untuk Spinner
        val optionsKontenSeksual = arrayOf("Pilih masalah", "Pornografi", "Eksploitasi anak", "Pelecehan seksual")
        val optionsKontenKekerasan = arrayOf("Pilih masalah", "Kekerasan fisik", "Kekerasan verbal", "Kekerasan psikologis")
        val optionsKontenKebencian = arrayOf("Pilih masalah", "Pelecehan rasial", "Pelecehan agama", "Pelecehan seksual")
        val optionsTindakanBerbahaya = arrayOf("Pilih masalah", "Penggunaan narkoba", "Penyalahgunaan senjata", "Tindakan berbahaya lainnya")
        val optionsSpam = arrayOf("Pilih masalah", "Berita palsu", "Iklan tidak sah", "Penipuan")
        val optionsHukum = arrayOf("Pilih masalah", "Pelanggaran hak cipta", "Pelanggaran privasi", "Masalah hukum lainnya")
        val optionsTeks = arrayOf("Pilih masalah", "Kata-kata kasar", "Teks diskriminatif", "Teks mengandung kekerasan")

        // Setup Spinner Adapters
        binding.spinnerKontenSeksual.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsKontenSeksual)
        binding.spinnerKontenKekerasan.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsKontenKekerasan)
        binding.spinnerKontenKebencian.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsKontenKebencian)
        binding.spinnerTindakanBerbahaya.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsTindakanBerbahaya)
        binding.spinnerSpam.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsSpam)
        binding.spinnerHukum.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsHukum)
        binding.spinnerTeks.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsTeks)

        // Membuat listener untuk setiap spinner
        val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                checkButtonState() // Memeriksa status tombol ketika ada item yang dipilih
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                checkButtonState() // Memeriksa status tombol jika tidak ada item yang dipilih
            }
        }

        // Tambahkan listener untuk mendeteksi perubahan pada spinner
        binding.spinnerKontenSeksual.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerKontenKekerasan.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerKontenKebencian.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerTindakanBerbahaya.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerSpam.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerHukum.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerTeks.onItemSelectedListener = spinnerItemSelectedListener

        // Tampilkan Spinner sesuai RadioButton yang dipilih
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // Sembunyikan semua spinner terlebih dahulu
            binding.spinnerKontenSeksual.visibility = View.GONE
            binding.spinnerKontenKekerasan.visibility = View.GONE
            binding.spinnerKontenKebencian.visibility = View.GONE
            binding.spinnerTindakanBerbahaya.visibility = View.GONE
            binding.spinnerSpam.visibility = View.GONE
            binding.spinnerHukum.visibility = View.GONE
            binding.spinnerTeks.visibility = View.GONE

            // Tampilkan spinner yang sesuai dengan pilihan RadioButton
            when (checkedId) {
                binding.radioKontenSeksual.id -> binding.spinnerKontenSeksual.visibility = View.VISIBLE
                binding.radioKontenKekerasan.id -> binding.spinnerKontenKekerasan.visibility = View.VISIBLE
                binding.radioKontenKebencian.id -> binding.spinnerKontenKebencian.visibility = View.VISIBLE
                binding.radioTindakanBerbahaya.id -> binding.spinnerTindakanBerbahaya.visibility = View.VISIBLE
                binding.radioSpam.id -> binding.spinnerSpam.visibility = View.VISIBLE
                binding.radioHukum.id -> binding.spinnerHukum.visibility = View.VISIBLE
                binding.radioTeks.id -> binding.spinnerTeks.visibility = View.VISIBLE
            }

            // Periksa status tombol "Berikutnya"
            checkButtonState()
        }

        // Tombol Berikutnya
        binding.buttonBerikutnya.setOnClickListener {
            binding.layoutStep1.visibility = View.GONE
            binding.layoutStep2.visibility = View.VISIBLE
        }

        // Tombol Kembali
        binding.buttonKembali.setOnClickListener {
            binding.layoutStep2.visibility = View.GONE
            binding.layoutStep1.visibility = View.VISIBLE
        }

        // Tombol Laporkan
        binding.buttonLaporkan.setOnClickListener {
            // Mendapatkan berita ID dari argumen
            val beritaId = arguments?.getString(ARG_ID_BERITA)

            // Mendapatkan pesan dari spinner yang dipilih
            val selectedRadioButtonId = binding.radioGroup.checkedRadioButtonId
            val selectedProblem = when (selectedRadioButtonId) {
                binding.radioKontenSeksual.id -> binding.spinnerKontenSeksual.selectedItem.toString()
                binding.radioKontenKekerasan.id -> binding.spinnerKontenKekerasan.selectedItem.toString()
                binding.radioKontenKebencian.id -> binding.spinnerKontenKebencian.selectedItem.toString()
                binding.radioTindakanBerbahaya.id -> binding.spinnerTindakanBerbahaya.selectedItem.toString()
                binding.radioSpam.id -> binding.spinnerSpam.selectedItem.toString()
                binding.radioHukum.id -> binding.spinnerHukum.selectedItem.toString()
                binding.radioTeks.id -> binding.spinnerTeks.selectedItem.toString()
                else -> ""
            }

            // Mendapatkan detail pesan dari EditText
            val detailPesan = binding.edittextDetailLaporan.text.toString()

            if (!beritaId.isNullOrEmpty() && !selectedProblem.isNullOrEmpty()) {
                // Mengambil userId dari repository
                userId = userRepository.getUserUid()
                if (userId.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "User belum login!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Membuat laporan
                reportBeritaViewModel.createLaporanBerita(userId!!, beritaId, selectedProblem, detailPesan)

                // Observasi hasil laporan
                reportBeritaViewModel.responseLaporan.observe(viewLifecycleOwner, { response ->
                    if (response != null && response.status == "success") {
                        Toast.makeText(requireContext(), "Laporan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                        dismiss() // Menutup dialog setelah laporan berhasil
                    } else {
                        Toast.makeText(requireContext(), "Gagal mengirim laporan", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        // Inisialisasi UserRepository
        userRepository = UserRepository(requireContext())
        userId = userRepository.getUserUid() ?: run {
            Toast.makeText(requireContext(), "User belum login!", Toast.LENGTH_SHORT).show()
            return // Jika userId null, hentikan proses lebih lanjut
        }
    }

    private fun checkButtonState() {
        val selectedRadioButtonId = binding.radioGroup.checkedRadioButtonId
        val spinner = when (selectedRadioButtonId) {
            binding.radioKontenSeksual.id -> binding.spinnerKontenSeksual
            binding.radioKontenKekerasan.id -> binding.spinnerKontenKekerasan
            binding.radioKontenKebencian.id -> binding.spinnerKontenKebencian
            binding.radioTindakanBerbahaya.id -> binding.spinnerTindakanBerbahaya
            binding.radioSpam.id -> binding.spinnerSpam
            binding.radioHukum.id -> binding.spinnerHukum
            binding.radioTeks.id -> binding.spinnerTeks
            else -> null
        }

        // Cek apakah ada pilihan yang valid
        if (spinner != null && spinner.selectedItemPosition > 0) {  // Pastikan item yang dipilih bukan item default "Pilih masalah"
            binding.buttonBerikutnya.isEnabled = true
            binding.buttonBerikutnya.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        } else {
            binding.buttonBerikutnya.isEnabled = false
            binding.buttonBerikutnya.setBackgroundColor(resources.getColor(R.color.gray))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
