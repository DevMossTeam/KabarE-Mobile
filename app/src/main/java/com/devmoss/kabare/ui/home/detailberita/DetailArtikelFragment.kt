package com.devmoss.kabare.ui.home.detailberita

import android.content.Intent
import android.graphics.BitmapFactory
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResultBookmark
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.databinding.FragmentDetailArtikelBinding
import com.devmoss.kabare.ui.reaksi.reaksiadapters.DaftarBeritaBookmarkAdapter
import com.devmoss.kabare.ui.reaksi.reaksiviewmodels.BookmarkViewModel
import com.devmoss.kabare.ui.home.detailberita.detailviewmodels.BeritaTerkaitViewModel
import com.devmoss.kabare.ui.home.detailberita.detailviewmodels.DetailArtikelViewModel
import com.devmoss.kabare.ui.komentarartikel.KomentarDialogFragment
import com.devmoss.kabare.ui.reaksi.reaksiviewmodels.ViewCountViewModel
import com.devmoss.kabare.ui.report.ReportDialogFragment
import com.devmoss.kabare.ui.search.SearchViewModel
import org.jsoup.Jsoup

class DetailArtikelFragment : Fragment() {
    private lateinit var binding: FragmentDetailArtikelBinding
    private var beritaId: String = ""

    // Mendeklarasikan ViewModel
    private val viewModel: DetailArtikelViewModel by viewModels()
    private val incrementViewCount: ViewCountViewModel by viewModels()
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()
    private val beritaTerkaitViewModel: BeritaTerkaitViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var beritaTerkaitAdapter: DaftarBeritaBookmarkAdapter

    //mengambil user id
    private lateinit var userRepository: UserRepository
    private var userId: String? = null
    private var savedScrollPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailArtikelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshContent()
        }
        binding.konten.visibility = View.GONE
        binding.progresBar.visibility = View.GONE

        // Ambil beritaId dari arguments
        arguments?.getString("beritaId")?.let {
            beritaId = it
            getDetailBerita(beritaId)}

        setupReaksiButtons()

        // Ubah warna icon back ke white
        binding.back.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))

        // Set listener untuk tombol back
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        // Menambahkan listener klik pada TextView setFont
        binding.setFont.setOnClickListener {
            showFontSizeMenu(it)
        }


        // Setup share button
        binding.shere.setOnClickListener {
            shareBerita()
        }

        // Mengaktifkan JavaScript di WebView
        binding.webViewKonten.settings.javaScriptEnabled = true

        // Default size text
        binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        updateTextSizeKonten(16f)

        // Inisialisasi UserRepository
        userRepository = UserRepository(requireContext())
        userId = userRepository.getUserUid() ?: run {
            return // Jika userId null, hentikan proses lebih lanjut
        }
    }

    private fun shareBerita() {
        val beritaTitle = binding.tvTitle.text.toString() // Judul berita
        val beritaUrl = "https://kabare.my.id/category/news-detail.php?id=$beritaId"
        val deepLinkUri = "kabare://news-detail/id/$beritaId"

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "$beritaTitle\n\nBaca selengkapnya di aplikasi Kabare: $deepLinkUri\n\nAtau di website: $beritaUrl"
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Bagikan Berita"))
    }

    private fun refreshContent() {
        binding.swipeRefreshLayout.isRefreshing = true

        // Contoh panggilan ulang data
        getDetailBerita(beritaId)
        getJumlahReaksi()
        getStatusLike()

        // Berhenti animasi refresh setelah data selesai dimuat
        binding.swipeRefreshLayout.isRefreshing = false
    }

    // Fungsi untuk mendapatkan status like dan mengupdate UI
    private fun getStatusLike() {
        viewModel.getStatusLike(userId ?: "", beritaId)
        viewModel.statusLike.observe(viewLifecycleOwner, Observer { response ->
            response?.let { statusResponse ->
                if (statusResponse.isLike == 1) {
                    binding.suka.setImageResource(R.drawable.ic_like_filled)
                    binding.tvLike.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                }else{
                    binding.suka.setImageResource(R.drawable.ic_like)
                }
                if (statusResponse.isDislike == 1) {
                    binding.icDislike.setImageResource(R.drawable.ic_dislike_filled)
                    binding.tvDisike.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                }else {
                    binding.icDislike.setImageResource(R.drawable.ic_dislike) // Icon dislike default
                }
            }
        })
    }

    private fun setupReaksiButtons() {
        binding.like.setOnClickListener {
            viewModel.sendReaksi(beritaId, userId?: "", "Suka") { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                getJumlahReaksi()
                getStatusLike()
            }
        }
        binding.dislike.setOnClickListener {
            viewModel.sendReaksi(beritaId, userId?: "", "Tidak Suka") { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                getJumlahReaksi()
                getStatusLike()
            }
        }
    }

    private fun setupBookmarkButton(beritaId: String, userId: String) {

        // Observe perubahan status bookmark dari ViewModel
        bookmarkViewModel.bookmarkStatusMap.observe(viewLifecycleOwner) { statusMap ->
            val isBookmarked = statusMap[beritaId] ?: false
            if(userId==""){
                return@observe
            }else{
                updateBookmarkIcon(isBookmarked)
            }
        }
        // Tambahkan listener untuk tombol bookmark
        binding.ivBookmark.setOnClickListener {
            val bookmark = ResultBookmark(userId, beritaId)
            if(userId == ""){
                Toast.makeText(requireContext(), "Silahkan login terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                bookmarkViewModel.toggleBookmark(bookmark, requireContext())
            }
        }
        // Memeriksa status awal bookmark jika belum ada di statusMap
        bookmarkViewModel.checkBookmarkStatus(userId, listOf(ListBerita(idBerita = beritaId)))
    }
    private fun updateBookmarkIcon(isBookmarked: Boolean) {
        binding.ivBookmark.setImageResource(
            if (isBookmarked) R.drawable.bookmark_true else R.drawable.bookmark_false
        )
    }

    private fun getJumlahReaksi() {
        viewModel.getJumlahReaksi(beritaId) { response ->
            if (response != null) {
                binding.tvLike.text = response.result?.jumlahSuka?.toString() ?: "0"
                binding.tvDisike.text = response.result?.jumlahTidakSuka?.toString() ?: "0"
            } else {
                Toast.makeText(
                    requireContext(),
                    "Gagal mendapatkan jumlah reaksi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Mengubah metode untuk menampilkan tag dan memberikan listener pada tag
    private fun updateLabels(labels: List<String>) {
        binding.labelContainer.removeAllViews()
        // Menambahkan tag baru ke dalam GridLayout
        for (label in labels) {
            val labelCard = LayoutInflater.from(context)
                .inflate(R.layout.item_label, binding.labelContainer, false) as CardView
            val labelTextView = labelCard.findViewById<TextView>(R.id.labelTextView)
            labelTextView.text = label
            // Set listener untuk setiap tag yang diklik
            labelTextView.setOnClickListener {
                searchViewModel.loadSearch(null, null, label)
                findNavController().navigate(R.id.action_detailArtikelFragment_to_searchResultsFragment)
            }
            // Menambahkan tag ke dalam container
            binding.labelContainer.addView(labelCard)
        }
    }


    private fun getDetailBerita(beritaId: String) {
        beritaTerkaitViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showShimmerEffect(isLoading)
        })
        // Memanggil ViewModel untuk mengambil detail berita
        viewModel.getDetailBerita(beritaId)
        // Observe perubahan data berita
        viewModel.beritaDetail.observe(viewLifecycleOwner, Observer { detail ->
            detail?.let {
                // Mengambil data dari response dan menampilkannya
                val berita = it.firstOrNull()
                berita?.let { item ->
                    // Set informasi artikel
                    binding.tvTitle.text = item.judul
                    binding.tvTimestamp.text = item.tanggalTerbit
                    binding.tvKategori.text = item.kategori
                    binding.tvAuthor.text = item.author
                    binding.tvLike.text = item.jumlahLike.toString()
                    binding.tvDisike.text = item.jumlahDislike.toString()

                    getBeritaTerkait(item.kategori ?: "", item.idBerita ?: "")

                    // Menampilkan gambar profil jika ada
                    if (!item.profilePic.isNullOrEmpty()) {
                        val imageBytes = Base64.decode(item.profilePic, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)

                        Glide.with(binding.webViewKonten.context)
                            .load(bitmap)
                            .into(binding.ivAuthor)
                    }

                    // Menampilkan gambar pertama di TopImage dan menghapusnya dari konten
                    item.kontenBerita?.let { kontenHtml ->
                        val document = Jsoup.parse(kontenHtml)
                        // Cari gambar pertama
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
                            // Hapus gambar pertama dari konten
                            img.remove()
                        }
                        // Tambahkan CSS untuk memastikan margin/padding terhapus
                        val cssStyle = """
                            <style>
                                body {
                                    margin: 0;
                                    padding: 0;
                                    line-height: 1.5;
                                }
                                img + * {
                                    margin-top: 0 !important;
                                    padding-top: 0 !important;
                                }
                                br {
                                    display: none !important;
                                }
                            </style>
                        """

                        // Gabungkan CSS dengan HTML
                        val modifiedHtml = """
                            <!DOCTYPE html>
                            <html>
                            <head>
                                $cssStyle
                            </head>
                            <body>
                                ${document.html()}
                            </body>
                            </html>
                        """

                        // Load konten yang dimodifikasi ke WebView
                        binding.webViewKonten.loadDataWithBaseURL(
                            null,
                            modifiedHtml,
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
                        val dialog = KomentarDialogFragment.newInstance(
                            berita.idBerita ?: "",
                            berita.jumlahKomentar ?: 0)
                        if(userId == null){
                            Toast.makeText(requireContext(), "Silahkan login terlebih dahulu!", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }else{
                            dialog.show(childFragmentManager, "KomentarDialogFragment")
                        }
                    }
                    // Set listener untuk membuka dialog komentar
                    binding.btnLaporan.setOnClickListener {
                            if (userId.isNullOrEmpty()) {
                                Toast.makeText(requireContext(), "Silahkan login terlebih dahulu!", Toast.LENGTH_SHORT).show()
                            } else {
                                val dialogLaporan = ReportDialogFragment.newInstance(berita.idBerita ?: "", null)
                                dialogLaporan.show(childFragmentManager, "ReportDialogFragment")
                            }
                        }
                    setupBookmarkButton(beritaId, userId?: "")
                    binding.konten.visibility = View.VISIBLE
                    binding.shimmerDetailBerita.visibility = View.GONE

                    // Panggil getStatusLike setelah detail artikel berhasil dimuat
                    getStatusLike()
//                    panggil increment view count
                    incrementViewCount.incrementViewCount(beritaId)
                }
            }
        })
    }

    private fun getBeritaTerkait(kategori: String, beritaId: String) {
        beritaTerkaitAdapter =  DaftarBeritaBookmarkAdapter(
            emptyList(),
            object : DaftarBeritaBookmarkAdapter.OnItemClickListener {
                override fun onBeritaBookmarkClick(beritaBookmark: ListBerita) {
                    val navOptions = NavOptions.Builder()
                        .setEnterAnim(android.R.anim.slide_in_left)
                        .setExitAnim(android.R.anim.slide_out_right)
                        .build()
                    val bundle = Bundle().apply {
                        putString("beritaId", beritaBookmark.idBerita) // Kirim ID Berita
                    }
                    requireParentFragment().findNavController().navigate(
                        R.id.action_detailArtikelFragment_self, bundle,navOptions)
                }
            })

        binding.rvBeritaTerkait.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = beritaTerkaitAdapter
        }

        beritaTerkaitViewModel.beritaTerkaitList.observe(
            viewLifecycleOwner,
            Observer { beritaTerkait ->
                beritaTerkait?.let {
                    val filteredList = it.filter { berita -> berita.idBerita != beritaId }
                    beritaTerkaitAdapter.updateList(filteredList)
                                   }
            })
        beritaTerkaitViewModel.loadBeritaTerkait(kategori, beritaId)
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
            style.innerHTML = 'body { font-size: ${size}px !important; }';
            document.head.appendChild(style);
        """
        binding.webViewKonten.evaluateJavascript(jsScript, null)
    }
    private fun showShimmerEffect(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerDetailBerita.startShimmer()
            binding.shimmerDetailBerita.visibility = View.VISIBLE
            binding.konten.visibility = View.GONE
        } else {
            binding.shimmerDetailBerita.stopShimmer()
        }
    }

    override fun onPause() {
        super.onPause()
        // Simpan posisi scroll saat fragment di-pause
        savedScrollPosition = binding.nestedScrollView.scrollY
    }

    
}