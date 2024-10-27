package com.devmoss.kabare.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.GridLayout
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Inisialisasi GridLayouts untuk Riwayat Pencarian dan Topik Hangat
        val gridLayoutHistory = binding.gridLayoutHistory
        val gridLayoutHotTopics = binding.gridLayoutHotTopics

        // Set riwayat pencarian dan topik hangat
        val historyList = listOf("Berita 1", "Berita 2", "Berita 3", "Berita 4")
        val hotTopicsList = listOf("Topik 1", "Topik 2", "Topik 3", "Topik 4")

        addCardsToGridLayout(gridLayoutHistory, historyList)
        addCardsToGridLayout(gridLayoutHotTopics, hotTopicsList)

        return binding.root
    }

    // Fungsi untuk menambahkan CardView ke GridLayout
    private fun addCardsToGridLayout(gridLayout: GridLayout, items: List<String>) {
        for (item in items) {
            val cardView = createCardView(item)
            val layoutParams = GridLayout.LayoutParams().apply {
                width = 0 // Mengatur CardView untuk mengambil ruang sama rata
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(16, 16, 16, 16)
            }
            gridLayout.addView(cardView, layoutParams)
        }
    }

    // Fungsi untuk membuat CardView
    private fun createCardView(text: String): CardView {
        val cardView = LayoutInflater.from(requireContext()).inflate(R.layout.card_item, null) as CardView
        val textView = cardView.findViewById<TextView>(R.id.cardText)
        textView.text = text
        return cardView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
