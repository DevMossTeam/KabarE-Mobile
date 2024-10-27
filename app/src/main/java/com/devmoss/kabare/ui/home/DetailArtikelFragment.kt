package com.devmoss.kabare.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.databinding.FragmentDetailArtikelBinding
import com.devmoss.kabare.utils.DateUtils

class DetailArtikelFragment : Fragment() {
    private lateinit var binding : FragmentDetailArtikelBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailArtikelBinding.inflate(inflater,container,false)

        //MENGUBAH WARNA ICON BACK KE WHITE
        binding.ivBack.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))

        // Ambil artikel dari arguments terkini
        val articleTerkini = arguments?.getParcelable<Artikel>("articleTerkini")
        articleTerkini?.let {
            // Set kategori ke TextView
            binding.tvKategori.text = it.kategori
        }
        // Ambil artikel dari arguments terkini
        val articleTeratas = arguments?.getParcelable<Artikel>("articleTeratas")
        articleTeratas?.let {
            // Set kategori ke TextView
            binding.tvKategori.text = it.kategori
        }

        // Set listener untuk tombol back (ImageView)
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed() // Kembali ke fragment sebelumnya
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil artikel dari arguments
        val article = arguments?.getParcelable<Artikel>("articleTerkini") ?: arguments?.getParcelable<Artikel>("articleTeratas")

        article?.let {
            // Tampilkan data artikel di UI
            binding.tvTitle.text = it.title
            binding.tvDescription.text = it.description
            binding.tvLike.text = it.jumlahLike.toString()
            binding.tvDisike.text = it.jumlahDislike.toString()
            binding.tvShare.text = it.jumlahShare.toString()
            binding.tvTimestamp.text = DateUtils.formatTimestamp(it.timestamp)
            binding.tvKategori.text = it.kategori

            // Tampilkan label secara dinamis
            updateLabels(it.label)
        }
    }

    private fun updateLabels(labels: List<String>) {
        // Bersihkan semua label yang ada
        binding.labelContainer.removeAllViews()

        // Tambahkan label dinamis ke GridLayout
        for (label in labels) {
            val labelCard = LayoutInflater.from(context).inflate(R.layout.item_label, binding.labelContainer, false) as CardView
            val labelTextView = labelCard.findViewById<TextView>(R.id.labelTextView)
            labelTextView.text = label

            // Tambahkan card ke dalam GridLayout
            binding.labelContainer.addView(labelCard)
        }
    }
}