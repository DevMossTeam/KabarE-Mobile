package com.devmoss.kabare.ui.home.terbaru.terbaruadapters

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.ItemBeritaTerkiniBinding
import android.text.Html


class BeritaTerkiniAdapter(
    private var beritaTerkini: List<ListBerita>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<BeritaTerkiniAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBeritaTerkiniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beritaTerkini = beritaTerkini[position]

        Log.d("BeritaTerkiniAdapter", "Binding item at position $position: ${beritaTerkini.judul}")

        holder.binding.tvTimestamp.text = beritaTerkini.tanggalDiterbitkan
        holder.binding.tvTitle.text = beritaTerkini.judul
        holder.binding.tvAuthor.text = beritaTerkini.author


        // Hanya menampilkan teks dari kontenBerita dengan menghapus semua tag HTML
        val konten = beritaTerkini.kontenBerita ?: ""
        val plainText = konten.replace(Regex("<[^>]*>"), "") // Menghapus semua tag HTML
        holder.binding.kontenBerita.text = plainText


        // Menampilkan gambar pertama dari kontenBerita jika ada
        val firstImageSrc = Regex("""<img\s+[^>]*src=["']([^"']+)["']""")
            .find(beritaTerkini.kontenBerita ?: "")
            ?.groupValues?.get(1) // Mendapatkan URL gambar pertama

        if (!firstImageSrc.isNullOrEmpty()) {
            // Jika gambar ditemukan, muat gambar menggunakan Glide
            Glide.with(holder.itemView.context)
                .load(firstImageSrc)
                .into(holder.binding.ivArticleImage)
        }


        // Menampilkan profilePic jika ada
        if (!beritaTerkini.profilePic.isNullOrEmpty()) {
            val imageBytes = Base64.decode(beritaTerkini.profilePic, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            Glide.with(holder.itemView.context)
                .load(bitmap)
                .into(holder.binding.ivAuthor)
        }

        // Menangani klik pada item berita
        holder.binding.cardView.setOnClickListener {
            listener.onArticleTerkiniClick(beritaTerkini)
        }

        // Menangani klik pada ikon bookmark
        holder.binding.ivBookmark.setOnClickListener {
            listener.onBookmarkBeritaTerkiniClick(beritaTerkini)

        }

        // Menentukan ikon bookmark berdasarkan status isBookmarked (Int)
        if (beritaTerkini.isBookmarked == 1) {
            holder.binding.ivBookmark.setImageResource(R.drawable.bookmark_true)
        } else {
            holder.binding.ivBookmark.setImageResource(R.drawable.bookmark_false)
        }

        // Menghapus semua label yang ada sebelum menambah yang baru
        holder.binding.labelContainer.removeAllViews()
        // Menambahkan label dinamis
        for (label in beritaTerkini.tags?.split(",") ?: emptyList()) {
            val labelCard = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_label, holder.binding.labelContainer, false) as CardView
            val labelTextView = labelCard.findViewById<TextView>(R.id.labelTextView)
            labelTextView.text = label
            holder.binding.labelContainer.addView(labelCard)
        }
    }

    override fun getItemCount(): Int {
        return beritaTerkini.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ListBerita>) {
        Log.d("BeritaTerkiniAdapter", "Received new list with ${newList.size} items")
        beritaTerkini = newList
        notifyDataSetChanged() // Memperbarui tampilan
    }

    class ViewHolder(val binding: ItemBeritaTerkiniBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBookmarkBeritaTerkiniClick(beritaTerkini: ListBerita)
        fun onArticleTerkiniClick(beritaTerkini: ListBerita)
    }
}
