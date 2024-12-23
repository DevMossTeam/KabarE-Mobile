package com.devmoss.kabare.ui.report

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReportDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentReportDialogBinding? = null
    private val binding get() = _binding!!

    // inisialisasi user repo
    private lateinit var userRepository: UserRepository
    private var userId: String? = null

    // ViewModel untuk Report Berita
    private val reportViewModel: ReportViewModel by viewModels()

    companion object {
        private const val ARG_ID_BERITA = "id_berita"
        private const val ARG_KOMENTAR_ID = "komentar_id"



        fun newInstance(idBerita: String?, komentarId: String?): ReportDialogFragment {
            val fragment = ReportDialogFragment()
            val args = Bundle()
            args.putString(ARG_ID_BERITA, idBerita)
            args.putString(ARG_KOMENTAR_ID, komentarId)  // Menambahkan komentarId
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

        val beritaId = arguments?.getString(ARG_ID_BERITA)
        val komentarId = arguments?.getString(ARG_KOMENTAR_ID)

        if (!beritaId.isNullOrEmpty() && !komentarId.isNullOrEmpty()) {
            binding.radioGroupReportKomentar.visibility = View.VISIBLE
            binding.radioGroupReportBerita.visibility = View.GONE
        } else {
            binding.radioGroupReportKomentar.visibility = View.GONE
            binding.radioGroupReportBerita.visibility = View.VISIBLE
        }


        // Data untuk Spinner report berita
        val optionsKontenSeksual = arrayOf("Pilih masalah", "Pornografi", "Eksploitasi anak", "Pelecehan seksual")
        val optionsKontenKekerasan = arrayOf("Pilih masalah", "Kekerasan fisik", "Kekerasan verbal", "Kekerasan psikologis")
        val optionsKontenKebencian = arrayOf("Pilih masalah", "Pelecehan rasial", "Pelecehan agama", "Pelecehan seksual")
        val optionsTindakanBerbahaya = arrayOf("Pilih masalah", "Penggunaan narkoba", "Penyalahgunaan senjata", "Tindakan berbahaya lainnya")
        val optionsSpam = arrayOf("Pilih masalah", "Berita palsu", "Iklan tidak sah", "Penipuan")
        val optionsHukum = arrayOf("Pilih masalah", "Pelanggaran hak cipta", "Pelanggaran privasi", "Masalah hukum lainnya")
        val optionsTeks = arrayOf("Pilih masalah", "Kata-kata kasar", "Teks diskriminatif", "Teks mengandung kekerasan")

        // Setup Spinner Adapters report berita
        binding.spinnerKontenSeksual.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsKontenSeksual)
        binding.spinnerKontenKekerasan.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsKontenKekerasan)
        binding.spinnerKontenKebencian.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsKontenKebencian)
        binding.spinnerTindakanBerbahaya.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsTindakanBerbahaya)
        binding.spinnerSpam.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsSpam)
        binding.spinnerHukum.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsHukum)
        binding.spinnerTeks.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsTeks)

        // Data untuk Spinner report komentar
        val optionsSpamKomentar = arrayOf("Pilih masalah", "Komentar mengandung iklan tidak relevan", "Komentar berisi tautan promosi", "Komentar terlalu sering diulang tanpa alasan jelas")
        val optionsPelecehanAtauBullying = arrayOf("Pilih masalah", "Komentar mengandung ancaman kepada pengguna lain", "Komentar berisi penghinaan personal", "Komentar digunakan untuk intimidasi kelompok tertentu")
        val optionsKebencianAtauDiskriminasi = arrayOf("Pilih masalah", "Mengandung ujaran kebencian terhadap tertentu", "Berisi diskriminasi terhadap agama / gender", "Mempromosikan kekerasan atau kebencian")
        val optionsKontenDewasaAtauTidakPantas = arrayOf("Pilih masalah", "Komentar mengandung kata-kata kasar", "Komentar berisi konten vulgar atau pornografi", "Komentar tidak sesuai dengan norma sosial")
        val optionsInformasiPalsuAtauMenyesatkan = arrayOf("Pilih masalah", "Komentar berisi klaim yang belum terverifikasi", "Komentar mengandung informasi hoaks", "Komentar mempromosikan penipuan")
        val optionsPelanggarPrivasi = arrayOf("Pilih masalah", "Komentar mengungkap informasi pribadi tanpa izin", "Komentar berisi data sensitif", "Komentar melanggar hak privasi orang lain")
        val optionsKomentarTidakRelevan = arrayOf("Pilih masalah", "Komentar keluar dari topik pembahasan", "Memprovokasi tanpa tujuan konstruktif.\n", "Komentar berisi makna yang tidak jelas")
        // Setup Spinner Adapters report berita
        binding.spinnerSpamKomentar.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsSpamKomentar)
        binding.spinnerPelecehanAtauBullying.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsPelecehanAtauBullying)
        binding.spinnerKebencianAtauDiskriminasi.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsKebencianAtauDiskriminasi)
        binding.spinnerKontenDewasaAtauTidakPantas.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsKontenDewasaAtauTidakPantas)
        binding.spinnerInformasiPalsuAtauMenyesatkan.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsInformasiPalsuAtauMenyesatkan)
        binding.spinnerPelanggaranPrivasi.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsPelanggarPrivasi)
        binding.spinnerKomentarTidakRelevan.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, optionsKomentarTidakRelevan)

        // Membuat listener untuk setiap spinner report berita
        val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                checkButtonState() // Memeriksa status tombol ketika ada item yang dipilih
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                checkButtonState() // Memeriksa status tombol jika tidak ada item yang dipilih
            }
        }

        // Tambahkan listener untuk mendeteksi perubahan pada spinner report berita
        binding.spinnerKontenSeksual.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerKontenKekerasan.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerKontenKebencian.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerTindakanBerbahaya.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerSpam.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerHukum.onItemSelectedListener = spinnerItemSelectedListener
        binding.spinnerTeks.onItemSelectedListener = spinnerItemSelectedListener

        // Tampilkan Spinner sesuai RadioButton report berita yang dipilih
        binding.radioGroupReportBerita.setOnCheckedChangeListener { _, checkedId ->
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
        /////////////////////////////////////

        // Membuat listener untuk setiap spinner report komentar
        val spinnerItemSelectedListenerReportKomentar = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                checkButtonStateReportKomentar() // Memeriksa status tombol ketika ada item yang dipilih
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                checkButtonStateReportKomentar() // Memeriksa status tombol jika tidak ada item yang dipilih
            }
        }
        // Tambahkan listener untuk mendeteksi perubahan pada spinner report komentar
        binding.spinnerSpamKomentar.onItemSelectedListener = spinnerItemSelectedListenerReportKomentar
        binding.spinnerPelecehanAtauBullying.onItemSelectedListener = spinnerItemSelectedListenerReportKomentar
        binding.spinnerKebencianAtauDiskriminasi.onItemSelectedListener = spinnerItemSelectedListenerReportKomentar
        binding.spinnerKontenDewasaAtauTidakPantas.onItemSelectedListener = spinnerItemSelectedListenerReportKomentar
        binding.spinnerInformasiPalsuAtauMenyesatkan.onItemSelectedListener = spinnerItemSelectedListenerReportKomentar
        binding.spinnerPelanggaranPrivasi.onItemSelectedListener = spinnerItemSelectedListenerReportKomentar
        binding.spinnerKomentarTidakRelevan.onItemSelectedListener = spinnerItemSelectedListenerReportKomentar

        // Tampilkan Spinner sesuai RadioButton report komentar yang dipilih
        binding.radioGroupReportKomentar.setOnCheckedChangeListener { _, checkedId ->
            // Sembunyikan semua spinner terlebih dahulu
            binding.spinnerSpamKomentar.visibility = View.GONE
            binding.spinnerPelecehanAtauBullying.visibility = View.GONE
            binding.spinnerKebencianAtauDiskriminasi.visibility = View.GONE
            binding.spinnerKontenDewasaAtauTidakPantas.visibility = View.GONE
            binding.spinnerInformasiPalsuAtauMenyesatkan.visibility = View.GONE
            binding.spinnerPelanggaranPrivasi.visibility = View.GONE
            binding.spinnerKomentarTidakRelevan.visibility = View.GONE

            // Tampilkan spinner yang sesuai dengan pilihan RadioButton
            when (checkedId) {
                binding.radioSpamKomentar.id -> binding.spinnerSpamKomentar.visibility = View.VISIBLE
                binding.radioPelecehanAtauBullying.id -> binding.spinnerPelecehanAtauBullying.visibility = View.VISIBLE
                binding.radioKebencianAtauDiskriminasi.id -> binding.spinnerKebencianAtauDiskriminasi.visibility = View.VISIBLE
                binding.radioKontenDewasaAtauTidakPantas.id -> binding.spinnerKontenDewasaAtauTidakPantas.visibility = View.VISIBLE
                binding.radioInformasiPalsuAtauMenyesatkan.id -> binding.spinnerInformasiPalsuAtauMenyesatkan.visibility = View.VISIBLE
                binding.radioPelanggaranPrivasi.id -> binding.spinnerPelanggaranPrivasi.visibility = View.VISIBLE
                binding.radioKomentarTidakRelevan.id -> binding.spinnerKomentarTidakRelevan.visibility = View.VISIBLE
            }

            // Periksa status tombol "Berikutnya"
            checkButtonStateReportKomentar()
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
            val beritaId = arguments?.getString(ARG_ID_BERITA)
            val komentarId = arguments?.getString(ARG_KOMENTAR_ID)

            // selected report berita
            val selectedRadioButtonId = binding.radioGroupReportBerita.checkedRadioButtonId
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

            // selected report komentar
            val selectedRadioButtonIdReportKomentar = binding.radioGroupReportKomentar.checkedRadioButtonId
            val selectedProblemReportKomentar = when (selectedRadioButtonIdReportKomentar) {
                binding.radioSpamKomentar.id -> binding.spinnerSpamKomentar.selectedItem.toString()
                binding.radioPelecehanAtauBullying.id -> binding.spinnerPelecehanAtauBullying.selectedItem.toString()
                binding.radioKebencianAtauDiskriminasi.id -> binding.spinnerKebencianAtauDiskriminasi.selectedItem.toString()
                binding.radioKontenDewasaAtauTidakPantas.id -> binding.spinnerKontenDewasaAtauTidakPantas.selectedItem.toString()
                binding.radioInformasiPalsuAtauMenyesatkan.id -> binding.spinnerInformasiPalsuAtauMenyesatkan.selectedItem.toString()
                binding.radioPelanggaranPrivasi.id -> binding.spinnerPelanggaranPrivasi.selectedItem.toString()
                binding.radioKomentarTidakRelevan.id -> binding.spinnerKomentarTidakRelevan.selectedItem.toString()
                else -> ""
            }

            val detailPesan = binding.edittextDetailLaporan.text.toString()

            // Memeriksa jika beritaId dan selectedProblem valid
            if (!beritaId.isNullOrEmpty() && selectedProblem != "Pilih masalah") {
                userId = userRepository.getUserUid()
                if (userId.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Silahkan login terlebih dahulu!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Jika komentarId ada dan selectedProblemReportKomentar valid, kirim laporan komentar
                if (!komentarId.isNullOrEmpty() && selectedProblemReportKomentar != "Pilih masalah") {
                    // Laporan komentar
                    reportViewModel.createLaporanKomentar(userId!!, beritaId, komentarId, selectedProblemReportKomentar, detailPesan)
                } else {
                    // Laporan berita
                    reportViewModel.createLaporanBerita(userId!!, beritaId, selectedProblem, detailPesan)
                }

                // Observasi hasil laporan
                reportViewModel.responseLaporan.observe(viewLifecycleOwner, { response ->
                    if (response != null && response.status == "success") {
                        Toast.makeText(requireContext(), "Laporan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Gagal mengirim laporan", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Menampilkan pesan error jika masalah belum dipilih
                Toast.makeText(requireContext(), "Pilih masalah terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize UserRepository
        userRepository = UserRepository(requireContext())
        userId = userRepository.getUserUid() ?: run {
            Toast.makeText(requireContext(), "Silahkan login terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return // Stop further processing if userId is null
        }
    }

    private fun checkButtonState() {
        val selectedRadioButtonId = binding.radioGroupReportBerita.checkedRadioButtonId
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

    private fun checkButtonStateReportKomentar() {
        val selectedRadioButtonIdReportKomentar = binding.radioGroupReportKomentar.checkedRadioButtonId
        // Tentukan spinner berdasarkan ID RadioButton yang terpilih
        val spinnerReportKomentar = when (selectedRadioButtonIdReportKomentar) {
            binding.radioSpamKomentar.id -> binding.spinnerSpamKomentar
            binding.radioPelecehanAtauBullying.id -> binding.spinnerPelecehanAtauBullying
            binding.radioKebencianAtauDiskriminasi.id -> binding.spinnerKebencianAtauDiskriminasi
            binding.radioKontenDewasaAtauTidakPantas.id -> binding.spinnerKontenDewasaAtauTidakPantas
            binding.radioInformasiPalsuAtauMenyesatkan.id -> binding.spinnerInformasiPalsuAtauMenyesatkan
            binding.radioPelanggaranPrivasi.id -> binding.spinnerPelanggaranPrivasi
            binding.radioKomentarTidakRelevan.id -> binding.spinnerKomentarTidakRelevan
            else -> null
        }
        // Cek apakah ada pilihan yang valid
        if (spinnerReportKomentar != null && spinnerReportKomentar.selectedItemPosition > 0) {
            // Pastikan item yang dipilih bukan item default "Pilih masalah"
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

