package com.devmoss.kabare.ui.home.terbaru.terbaruadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.ItemBeritaTerkiniBinding
import com.devmoss.kabare.ui.reaksi.reaksiviewmodels.BookmarkViewModel
import com.devmoss.kabare.utils.timeAgo


class BeritaTerkiniAdapter(
    private var beritaTerkini: List<ListBerita>,
    private val listener: OnItemClickListener,
    private val bookmarkViewModel: BookmarkViewModel,  // Tambahkan ViewModel
    private val userId: String  // ID User untuk mengecek status bookmark
) : RecyclerView.Adapter<BeritaTerkiniAdapter.ViewHolder>() {

    // Menyimpan status bookmark lokal
    private var bookmarkStatusMap: MutableMap<String, Boolean> = mutableMapOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBeritaTerkiniBinding.inflate(LayoutInflater.from(parent.context),parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beritaTerkini = beritaTerkini[position]
        holder.binding.tvTimestamp.text = timeAgo(beritaTerkini.tanggalDiterbitkan)

        holder.binding.tvTitle.text = beritaTerkini.judul
//        holder.binding.tvAuthor.text = beritaTerkini.author

        // Menampilkan profilePic jika ada
//        if (!beritaTerkini.profilePic.isNullOrEmpty()) {
//            val imageBytes = Base64.decode(beritaTerkini.profilePic, Base64.DEFAULT)
//            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//
//            Glide.with(holder.itemView.context)
//                .load(bitmap)
//                .into(holder.binding.ivAuthor)
//        }

        // Memuat konten berita, menghapus tag HTML
        val konten = beritaTerkini.kontenBerita ?: ""
//        val plainText = konten.replace(Regex("<[^>]*>"), "")
//        holder.binding.kontenBerita.text = plainText

        // Menampilkan gambar pertama dari kontenBerita jika ada
        val firstImageSrc = Regex("""<img\s+[^>]*src=["']([^"']+)["']""")
            .find(konten)?.groupValues?.get(1)
        if (!firstImageSrc.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(firstImageSrc).
            into(holder.binding.ivArticleImage)
        }

        // Menangani klik pada ikon bookmark
        holder.binding.ivBookmark.setOnClickListener {
            listener.onBookmarkBeritaTerkiniClick(beritaTerkini)
        }

        // Status bookmark dari map yang sudah dimuat
        val isBookmarked = bookmarkViewModel.bookmarkStatusMap.value?.
        get(beritaTerkini.idBerita.toString()) ?: false
        holder.binding.ivBookmark.setImageResource(
            if (isBookmarked) R.drawable.bookmark_true else R.drawable.bookmark_false
        )

        // Menangani klik pada item berita
        holder.binding.cardView.setOnClickListener {
            listener.onArticleTerkiniClick(beritaTerkini)
        }

        // Menambahkan label dinamis jika ada
        holder.binding.labelContainer.removeAllViews()
        for (label in beritaTerkini.tags?.split(",") ?: emptyList()) {
            val labelCard = LayoutInflater.from(holder.itemView.context).inflate(
                R.layout.item_label, holder.binding.labelContainer, false) as CardView
            val labelTextView = labelCard.findViewById<TextView>(R.id.labelTextView)
            labelTextView.text = label
            holder.binding.labelContainer.addView(labelCard)
        }
    }

    override fun getItemCount(): Int = beritaTerkini.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ListBerita>) {
        beritaTerkini = newList
        notifyDataSetChanged()  // Memperbarui tampilan
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


    class ViewHolder(val binding: ItemBeritaTerkiniBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBookmarkBeritaTerkiniClick(beritaTerkini: ListBerita)
        fun onArticleTerkiniClick(beritaTerkini: ListBerita)
    }
}


