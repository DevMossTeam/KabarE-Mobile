package com.devmoss.kabare.ui.home.terbaru

import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.FragmentDetailArtikelBinding
import com.devmoss.kabare.ui.home.terbaru.terbaruviewmodels.DetailArtikelViewModel
import com.devmoss.kabare.ui.komentarartikel.KomentarDialogFragment
import org.jsoup.Jsoup

class DetailArtikelFragment : Fragment() {
    private lateinit var binding: FragmentDetailArtikelBinding
    private var beritaId: String = ""
    private var userId: String = "2" // Ganti dengan ID pengguna yang sesuai

    // Mendeklarasikan ViewModel
    private val viewModel: DetailArtikelViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailArtikelBinding.inflate(inflater, container, false)

        // Ubah warna icon back ke white
        binding.ivBack.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))

        // Set listener untuk tombol back
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Menambahkan listener klik pada TextView setFont
        binding.setFont.setOnClickListener {
            showFontSizeMenu(it)
        }

        // Mengaktifkan JavaScript di WebView
        binding.webViewKonten.settings.javaScriptEnabled = true

        // Default size text
        binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        updateTextSizeKonten(16f)

        setupReaksiButtons()

        return binding.root
    }

    private fun setupReaksiButtons() {
        // Tombol Like
        binding.like.setOnClickListener {
            viewModel.sendReaksi(beritaId, userId, "Suka") { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                getJumlahReaksi()
            }
        }

        // Tombol Dislike
        binding.disike.setOnClickListener {
            viewModel.sendReaksi(beritaId, userId, "Tidak Suka") { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                getJumlahReaksi()
            }
        }
    }

    private fun getJumlahReaksi() {
        viewModel.getJumlahReaksi(beritaId) { response ->
            if (response != null) {
                binding.tvLike.text = response.result?.jumlahSuka?.toString() ?: "0"
                binding.tvDisike.text = response.result?.jumlahTidakSuka?.toString() ?: "0"
            } else {
                Toast.makeText(requireContext(), "Gagal mendapatkan jumlah reaksi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil artikel dari arguments
        val detailBerita = arguments?.getParcelable<ListBerita>("beritaArgument")
            ?: arguments?.getParcelable<ListBerita>("articleTeratas")
        beritaId = detailBerita?.idBerita ?: ""

        // Dapatkan jumlah reaksi untuk berita ini
        getJumlahReaksi()

        detailBerita?.let { berita ->
            // Set informasi artikel
            binding.tvTitle.text = berita.judul
            binding.tvTimestamp.text = berita.tanggalDiterbitkan
            binding.tvKategori.text = berita.kategori
            binding.tvAuthor.text = berita.author
            binding.tvLike.text = berita.jumlahLike.toString()
            binding.tvDisike.text = berita.jumlahDislike.toString()

            // Menampilkan gambar pertama di TopImage dan menghapusnya dari konten
            berita.kontenBerita?.let { kontenHtml ->
                val document = Jsoup.parse(kontenHtml)
                val imgTag = document.selectFirst("img")
                imgTag?.let { img ->
                    val imgSrc = img.attr("src")

                    // Jika gambar dalam format Base64
                    if (imgSrc.startsWith("data:image")) {
                        val base64Data = imgSrc.substringAfter("base64,")
                        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                        Glide.with(this)
                            .load(decodedBytes)
                            .into(binding.TopImage)
                    } else {
                        // Jika gambar dalam bentuk URL
                        Glide.with(this)
                            .load(imgSrc)
                            .into(binding.TopImage)
                    }
                    // Hapus gambar pertama dari konten HTML
                    img.remove()
                }

                // Menampilkan konten HTML berita di WebView tanpa gambar pertama
                binding.webViewKonten.loadDataWithBaseURL(
                    null,
                    document.html(), // Gunakan HTML yang telah diubah
                    "text/html",
                    "UTF-8",
                    null
                )
            }

            binding.webViewKonten.setBackgroundColor(Color.parseColor("#FAFAFA"))

            // Tambahkan label secara dinamis
            updateLabels(berita.tags?.split(",") ?: emptyList())

            // Set listener untuk membuka dialog komentar
            binding.btnKomentar.setOnClickListener {
                val dialog = KomentarDialogFragment.newInstance(berita.idBerita ?: "")
                dialog.show(childFragmentManager, "KomentarDialogFragment")
            }
        }
    }

    private fun updateLabels(labels: List<String>) {
        binding.labelContainer.removeAllViews()
        for (label in labels) {
            val labelCard = LayoutInflater.from(context)
                .inflate(R.layout.item_label, binding.labelContainer, false) as CardView
            val labelTextView = labelCard.findViewById<TextView>(R.id.labelTextView)
            labelTextView.text = label
            binding.labelContainer.addView(labelCard)
        }
    }

    // Fungsi untuk menampilkan menu ukuran font
    private fun showFontSizeMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.font_size_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.small -> {
                    binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    updateTextSizeKonten(10f)
                    true
                }

                R.id.medium -> {
                    binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                    updateTextSizeKonten(16f)  // Sedang
                    true
                }

                R.id.large -> {
                    binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                    updateTextSizeKonten(20f)  // Besar
                    true
                }

                R.id.very_large -> {
                    binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f)
                    updateTextSizeKonten(24f)  // Sangat Besar
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    // Fungsi untuk mengupdate ukuran teks di WebView
    private fun updateTextSizeKonten(size: Float) {
        val jsScript = """
            var style = document.createElement('style');
            style.innerHTML = 'body { font-size: ${size}px; }';
            document.head.appendChild(style);
        """
        binding.webViewKonten.evaluateJavascript(jsScript, null)
    }

}
