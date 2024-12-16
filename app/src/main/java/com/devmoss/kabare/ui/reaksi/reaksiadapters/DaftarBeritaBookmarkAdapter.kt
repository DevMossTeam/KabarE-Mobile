package com.devmoss.kabare.ui.reaksi.reaksiadapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.ItemBeritaBookmarkBinding
import com.devmoss.kabare.utils.timeAgo

class DaftarBeritaBookmarkAdapter(
    private var beritaBookmark: List<ListBerita>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DaftarBeritaBookmarkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding = ItemBeritaBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beritaBookmark = beritaBookmark[position]
        holder.binding.tvTitle.text = beritaBookmark.judul
        holder.binding.tvTimestamp.text = timeAgo(beritaBookmark.tanggalDiterbitkan)

        // Menampilkan gambar pertama dari konten berita jika ada
        val firstImageSrc = Regex("""<img\s+[^>]*src=["']([^"']+)["']""")
            .find(beritaBookmark.kontenBerita ?: "")
            ?.groupValues?.get(1)

        if (!firstImageSrc.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(firstImageSrc)
                .into(holder.binding.imageArticle)
            Log.d("BeritaTerkaitAdapter", "Gambar ditemukan: $firstImageSrc")
        } else {
            Log.d("BeritaTerkaitAdapter", "Gambar tidak ditemukan pada konten berita.")
        }

        holder.binding.articleConten.setOnClickListener {
            listener.onBeritaBookmarkClick(beritaBookmark)
        }
    }

    override fun getItemCount(): Int {
        return beritaBookmark.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newArticles: List<ListBerita>) {
        beritaBookmark = newArticles
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemBeritaBookmarkBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBeritaBookmarkClick(beritaBookmark: ListBerita)
    }
}