package com.devmoss.kabare.ui.home.populer.populeradapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.ItemBeritaPopulerBinding

class BeritaPopulerAdapter(
    private var beritaPopuler: List<ListBerita>,
    private val listener:OnItemClickListener

): RecyclerView.Adapter<BeritaPopulerAdapter.ViewHolder>() {

    val userIdLogin = "3"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemBeritaPopulerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val beritaPopuler = beritaPopuler[position]
        holder.binding.tvTitle.text =beritaPopuler.judul
        holder.binding.tvTime.text = beritaPopuler.tanggalDiterbitkan



        // Menampilkan gambar pertama dari kontenBerita jika ada
        val firstImageSrc = Regex("""<img\s+[^>]*src=["']([^"']+)["']""")
            .find(beritaPopuler.kontenBerita ?: "")
            ?.groupValues?.get(1) // Mendapatkan URL gambar pertama

        if (!firstImageSrc.isNullOrEmpty()) {
            // Jika gambar ditemukan, muat gambar menggunakan Glide
            Glide.with(holder.itemView.context)
                .load(firstImageSrc)
                .into(holder.binding.image)
        }

        // Set nomor urut pada TextView
        holder.binding.tvNomorUrut.text = "${position + 1}"

        holder.binding.ivBookmark.setOnClickListener {
            listener.onBookmarkBeritaPopulerClick(beritaPopuler)
        }
        // Menentukan ikon bookmark berdasarkan status isBookmarked (Int)
        if (beritaPopuler.isBookmarked == 1) {
            holder.binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            holder.binding.ivBookmark.setImageResource(R.drawable.ic_bookmark)
        }
        holder.binding.itemBeritaTeratasPopuler.setOnClickListener {
            listener.onArticlePopulerClick(beritaPopuler)
        }
    }

    override fun getItemCount(): Int {
        return beritaPopuler.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newArticles: List<ListBerita>) {
        beritaPopuler = newArticles
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemBeritaPopulerBinding) : RecyclerView.ViewHolder(binding.root)


    interface OnItemClickListener {
        fun onBookmarkBeritaPopulerClick(beritaPopuler: ListBerita)
        fun onArticlePopulerClick(beritaPopuler: ListBerita)

    }

}