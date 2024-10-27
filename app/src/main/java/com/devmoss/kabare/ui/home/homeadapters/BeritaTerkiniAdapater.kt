package com.devmoss.kabare.ui.home.homeadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.databinding.ItemBeritaTerkiniBinding
import com.devmoss.kabare.utils.DateUtils

class BeritaTerkiniAdapater(
    private var artikel: List<Artikel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<BeritaTerkiniAdapater.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBeritaTerkiniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beritaTerkini = artikel[position]

        holder.binding.tvTitle.text = beritaTerkini.title
        holder.binding.tvAuthor.text = beritaTerkini.authorProfile
        holder.binding.tvDescription.text = beritaTerkini.description
        holder.binding.tvTimestamp.text = DateUtils.formatTimestamp(beritaTerkini.timestamp)

        Glide.with(holder.itemView.context)
            .load(beritaTerkini.image)
            .into(holder.binding.ivArticleImage)

        holder.binding.cardView.setOnClickListener {
            listener.onArticleTerkiniClick(beritaTerkini) // Kirim artikel ke listener
        }

        // Ubah warna bookmark sesuai statusnya
        if (beritaTerkini.isBookmarked) {
            holder.binding.ivBookmark.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.bookmarkTrue))
            holder.binding.ivBookmark.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.bookmarkTrue))
        } else {
            // Setel drawable default ketika bookmark tidak aktif
            holder.binding.ivBookmark.setImageResource(R.drawable.ic_bookmark) // Ganti dengan drawable default yang diinginkan
            holder.binding.ivBookmark.clearColorFilter() // Menghapus filter warna jika ada
        }

        holder.binding.ivBookmark.setOnClickListener {
            listener.onBookmarkBeritaTerkiniClick(beritaTerkini)
        }

        // Menghapus semua label yang ada sebelum menambah yang baru
        holder.binding.labelContainer.removeAllViews()
        // Menambahkan label dinamis
        for (label in beritaTerkini.label) {
            val labelCard = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_label, holder.binding.labelContainer, false) as CardView
            val labelTextView = labelCard.findViewById<TextView>(R.id.labelTextView) // Pastikan Anda membuat layout item_label.xml

            labelTextView.text = label
            holder.binding.labelContainer.addView(labelCard)
        }
    }

    override fun getItemCount(): Int = artikel.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newArticles: List<Artikel>) {
        artikel = newArticles
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemBeritaTerkiniBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBookmarkBeritaTerkiniClick(article: Artikel)
        fun onArticleTerkiniClick(article: Artikel)
    }
}
