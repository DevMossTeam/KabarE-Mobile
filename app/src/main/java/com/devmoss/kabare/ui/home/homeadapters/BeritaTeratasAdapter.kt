package com.devmoss.kabare.ui.home.homeadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.databinding.ItemBeritaTeratasBinding
import com.devmoss.kabare.utils.DateUtils

class BeritaTeratasAdapter(
    private var artikel: List<Artikel>,
    private val listener: OnItemClickListener

) : RecyclerView.Adapter<BeritaTeratasAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeritaTeratasAdapter.ViewHolder {
        val binding = ItemBeritaTeratasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: BeritaTeratasAdapter.ViewHolder, position: Int) {
        val artikel = artikel[position]
        holder.binding.tvTitle.text =artikel.title
        holder.binding.tvTimestamp.text = DateUtils.formatTimestamp(artikel.timestamp)
        Glide.with(holder.itemView.context)
            .load(artikel.image)
            .into(holder.binding.imageArticle)

        // Logika untuk mengubah warna bookmark
        if (artikel.isBookmarked) {
            holder.binding.ivBookmark.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.bookmarkTrue))
            holder.binding.ivBookmark.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.bookmarkTrue))
        } else {
            // Setel drawable default ketika bookmark tidak aktif
            holder.binding.ivBookmark.setImageResource(R.drawable.ic_bookmark) // Ganti dengan drawable default yang diinginkan
            holder.binding.ivBookmark.clearColorFilter() // Menghapus filter warna jika ada
        }

        holder.binding.ivBookmark.setOnClickListener {
            listener.onBookmarkBeritaTeratasClick(artikel)
        }
        holder.binding.articleConten.setOnClickListener {
            listener.onArticleTeratasClick(artikel)
        }

    }
    override fun getItemCount(): Int {
        return artikel.size

    }
    fun updateList(newArticles: List<Artikel>) {
        artikel = newArticles
        notifyDataSetChanged()  // Beri tahu adapter bahwa data telah berubah

    }

    class ViewHolder(val binding: ItemBeritaTeratasBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBookmarkBeritaTeratasClick(article: Artikel)
        fun onArticleTeratasClick(article: Artikel)
    }
}