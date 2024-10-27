package com.devmoss.kabare.ui.manajemenartikel

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.devmoss.kabare.databinding.FragmentKonfirmasiPublikasiBinding

class KonfirmasiPublikasiFragment : Fragment() {
    private lateinit var binding: FragmentKonfirmasiPublikasiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKonfirmasiPublikasiBinding.inflate(inflater, container, false)

        // Set listener untuk tombol back (ImageView)
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed() // Kembali ke fragment sebelumnya
        }

        binding.tvDropdown.setOnClickListener {
            // Periksa visibilitas saat ini
            if (binding.visibilityRadioButton.visibility == View.GONE) {
                binding.visibilityRadioButton.visibility = View.VISIBLE
                binding.arrowBottom.visibility = View.GONE
                binding.arrowTop.visibility = View.VISIBLE
            } else {
                binding.visibilityRadioButton.visibility = View.GONE
                binding.arrowBottom.visibility = View.VISIBLE
                binding.arrowTop.visibility = View.GONE
            }
        }

        binding.labelDropdown.setOnClickListener {
            if (binding.rvLabel.visibility == View.GONE) {
                binding.rvLabel.visibility = View.VISIBLE
                binding.labelArrowBottom.visibility = View.GONE
                binding.labelArrowTop.visibility = View.VISIBLE
            } else {
                binding.rvLabel.visibility = View.GONE
                binding.labelArrowBottom.visibility = View.VISIBLE
                binding.labelArrowTop.visibility = View.GONE
            }
        }

        binding.kategoriDropdown.setOnClickListener {
            if (binding.rvKategori.visibility == View.GONE) {
                binding.rvKategori.visibility = View.VISIBLE
                binding.kategoriArrowBottom.visibility = View.GONE
                binding.kategoriArrowTop.visibility = View.VISIBLE
            } else {
                binding.rvKategori.visibility = View.GONE
                binding.kategoriArrowBottom.visibility = View.VISIBLE
                binding.kategoriArrowTop.visibility = View.GONE
            }
        }

        // Pencarian Kategori
        setupCategorySearch()

        // Menangani klik pada tombol untuk menambahkan label
        binding.btnAddLabel.setOnClickListener {
            // Tampilkan EditText dan fokus padanya
            binding.etLabel.visibility = View.VISIBLE
            binding.etLabel.requestFocus()

            // Tampilkan keyboard
            val imm = requireActivity().getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(binding.etLabel, InputMethodManager.SHOW_IMPLICIT)

            // Ambil teks dari EditText
            val labelText = binding.etLabel.text.toString().trim()

            // Jika teks tidak kosong
            if (labelText.isNotEmpty()) {
                // Pisahkan label berdasarkan koma
                val labels = labelText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                // Tambahkan setiap label ke grid
                labels.forEach { label ->
                    addLabelToGrid("#$label")
                }
                // Bersihkan EditText setelah menambahkan label
                binding.etLabel.text.clear()
            }
        }

        return binding.root
    }
    // Fungsi untuk setup pencarian kategori
    private fun setupCategorySearch() {
        // Ambil semua CheckBox kategori dari ViewBinding
        val checkboxes = listOf(
            binding.cbxPolitik,
            binding.cbxEkonomi,
            binding.cbxTeknologi,
            binding.cbxOlahraga,
            binding.cbxHiburan,
            binding.cbxKesehatan,
            binding.cbxPendidikan,
            binding.cbxGayahidup,
            binding.cbxInternasional,
            binding.cbxLingkungan
        )
        // Tambahkan TextWatcher pada EditText untuk pencarian kategori
        binding.etSearchKategori.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().lowercase()

                // Filter checkboxes berdasarkan teks pencarian
                for (checkbox in checkboxes) {
                    if (checkbox.text.toString().lowercase().contains(searchText)) {
                        checkbox.visibility = View.VISIBLE
                    } else {
                        checkbox.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun addLabelToGrid(label: String) {
        // Buat CardView untuk label baru
        val cardView = CardView(requireContext()).apply {
            // Atur layout params dan margin
            val params = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(7, 7, 7, 7) // Atur margin
            }
            layoutParams = params
            setCardBackgroundColor(Color.parseColor("#539EFF"))
            radius = 10f

            // Tambahkan OnClickListener untuk menghapus label
            setOnClickListener {
                binding.labelContainer.removeView(this) // Menghapus CardView dari GridLayout
            }
        }
        // Buat TextView untuk menampilkan label
        val textView = TextView(requireContext()).apply {
            text = label
            setTextColor(Color.WHITE)
            setPadding(7, 7, 7, 7)
        }

        // Tambahkan TextView ke CardView
        cardView.addView(textView)

        // Tambahkan CardView ke GridLayout
        binding.labelContainer.addView(cardView)
    }
}
