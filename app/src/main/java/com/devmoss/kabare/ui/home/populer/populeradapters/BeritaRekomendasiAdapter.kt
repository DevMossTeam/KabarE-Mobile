package com.devmoss.kabare.ui.home.populer.populeradapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.ItemBeritaRekomendasiBinding
import com.devmoss.kabare.ui.reaksi.reaksiviewmodels.BookmarkViewModel
import com.devmoss.kabare.utils.timeAgo

class BeritaRekomendasiAdapter(
    private var beritaRekomendasi: List<ListBerita>,
    private val listener:OnItemClickListener,
    private val bookmarkViewModel: BookmarkViewModel,  // Tambahkan ViewModel
    private val userId: String  // ID User untuk mengecek status bookmark

): RecyclerView.Adapter<BeritaRekomendasiAdapter.ViewHolder>() {

    // Menyimpan status bookmark lokal
    private var bookmarkStatusMap: MutableMap<String, Boolean> = mutableMapOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemBeritaRekomendasiBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val beritaRekomendasi = beritaRekomendasi[position]
        holder.binding.tvJudulBerita.text =beritaRekomendasi.judul
        holder.binding.tvTanggal.text = timeAgo(beritaRekomendasi.tanggalDiterbitkan)
        holder.binding.tvKategori.text = beritaRekomendasi.kategori

        // Menampilkan gambar pertama dari kontenBerita jika ada
        val firstImageSrc = Regex("""<img\s+[^>]*src=["']([^"']+)["']""")
            .find(beritaRekomendasi.kontenBerita ?: "")
            ?.groupValues?.get(1) // Mendapatkan URL gambar pertama

        if (!firstImageSrc.isNullOrEmpty()) {
            // Jika gambar ditemukan, muat gambar menggunakan Glide
            Glide.with(holder.itemView.context)
                .load(firstImageSrc)
                .into(holder.binding.ivBerita)
        }

        holder.binding.ivBookmark.setOnClickListener {
            listener.onBookmarkBeritaRekomendasiClick(beritaRekomendasi)
        }

        // Status bookmark dari map yang sudah dimuat
        val isBookmarked = bookmarkViewModel.bookmarkStatusMap.value?.
        get(beritaRekomendasi.idBerita.toString()) ?: false
        holder.binding.ivBookmark.setImageResource(
            if (isBookmarked) R.drawable.bookmark_true else R.drawable.bookmark_false
        )


        holder.binding.contentBerita.setOnClickListener {
            listener.onArticleRekomendasiClick(beritaRekomendasi)
        }

    }

    override fun getItemCount(): Int {
        return beritaRekomendasi.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateBeritaRekomendaList(newArticles: List<ListBerita>) {
        beritaRekomendasi = newArticles
        notifyDataSetChanged()
    }

    fun toggleBookmarkStatus(beritaId: String) {
        bookmarkStatusMap[beritaId] = !(bookmarkStatusMap[beritaId] ?: false)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateBookmarkStatusMap(statusMap: Map<String, Boolean>) {
        bookmarkStatusMap = statusMap.toMutableMap()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemBeritaRekomendasiBinding):RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBookmarkBeritaRekomendasiClick(beritaRekomendasi: ListBerita)
        fun onArticleRekomendasiClick(beritaRekomendasi: ListBerita)

    }

}