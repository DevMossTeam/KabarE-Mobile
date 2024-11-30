package com.devmoss.kabare.ui.home.populer.populeradapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.ItemBeritaRekomendasiBinding

class BeritaRekomendasiAdapter(
    private var beritaRekomendasi: List<ListBerita>,
    private val listener:OnItemClickListener

): RecyclerView.Adapter<BeritaRekomendasiAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemBeritaRekomendasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val beritaRekomendasi = beritaRekomendasi[position]
        holder.binding.tvJudulBerita.text =beritaRekomendasi.judul
        holder.binding.tvTanggal.text = beritaRekomendasi.tanggalDiterbitkan
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
        if (beritaRekomendasi.isBookmarked == 1) {
            holder.binding.ivBookmark.setImageResource(R.drawable.bookmark_true)
        } else {
            holder.binding.ivBookmark.setImageResource(R.drawable.bookmark_false)
        }

        holder.binding.contentBerita.setOnClickListener {
            listener.onArticleRekomendasiClick(beritaRekomendasi)
        }

    }

    override fun getItemCount(): Int {
        return beritaRekomendasi.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newArticles: List<ListBerita>) {
        beritaRekomendasi = newArticles
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemBeritaRekomendasiBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBookmarkBeritaRekomendasiClick(beritaRekomendasi: ListBerita)
        fun onArticleRekomendasiClick(beritaRekomendasi: ListBerita)

    }

}