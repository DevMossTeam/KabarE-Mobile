package com.devmoss.kabare.ui.home.terbaru.terbaruadapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.ItemBeritaTerkaitBinding

class BeritaTerkaitAdapter(
    private var beritaTerkait: List<ListBerita>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<BeritaTerkaitAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBeritaTerkaitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beritaTerkait = beritaTerkait[position]
        holder.binding.tvTitle.text = beritaTerkait.judul
        holder.binding.tvTimestamp.text = beritaTerkait.tanggalDiterbitkan

        // Menampilkan gambar pertama dari konten berita jika ada
        val firstImageSrc = Regex("""<img\s+[^>]*src=["']([^"']+)["']""")
            .find(beritaTerkait.kontenBerita ?: "")
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
            listener.onArticleTerkaitClick(beritaTerkait)
        }
        holder.binding.ivBookmark.setOnClickListener {
            listener.onBookmarkBeritaTerkaitClick(beritaTerkait)
        }
        // Menentukan ikon bookmark berdasarkan status isBookmarked (Int)
        if (beritaTerkait.isBookmarked == 1) {
            holder.binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            holder.binding.ivBookmark.setImageResource(R.drawable.ic_bookmark)
        }
    }

    override fun getItemCount(): Int {
        return beritaTerkait.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newArticles: List<ListBerita>) {
        Log.d("BeritaTerkaitAdapter", "Data baru diterima: $newArticles")
        beritaTerkait = newArticles
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemBeritaTerkaitBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBookmarkBeritaTerkaitClick(beritaTerkait: ListBerita)
        fun onArticleTerkaitClick(beritaTerkait: ListBerita)
    }
}
