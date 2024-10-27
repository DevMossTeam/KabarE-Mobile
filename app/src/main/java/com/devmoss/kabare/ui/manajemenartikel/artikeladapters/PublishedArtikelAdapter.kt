package com.devmoss.kabare.ui.manajemenartikel.artikeladapters

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.databinding.ItemPublishedArtikelBinding

class PublishedArtikelAdapter(
    private var publishedArtikelList: List<Artikel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PublishedArtikelAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPublishedArtikelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val publishedArtikel = publishedArtikelList[position]

        // Atur data artikel ke dalam view
        holder.binding.tvJudul.text = publishedArtikel.title
        holder.binding.tvTerakhirUpdate.text = publishedArtikel.tanggalDiperbarui

        // Atur gambar menggunakan Glide
        Glide.with(holder.itemView.context)
            .load(publishedArtikel.image) // Memuat gambar dari URL
            .into(holder.binding.image) // Set gambar ke ImageView

        // Klik pada judul artikel
        holder.binding.root.setOnClickListener {
            listener.onJudulClick(position)
        }

        // Klik pada tombol tiga titik (overflow menu)
        holder.binding.btnMore.setOnClickListener {
            showPopupMenu(holder.binding.btnMore, position)
        }
    }

    override fun getItemCount(): Int {
        return publishedArtikelList.size
    }

    fun updateData(newPublishedArtikelList: List<Artikel>) {
        publishedArtikelList = newPublishedArtikelList
        notifyDataSetChanged()
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu_draf_penulis, popupMenu.menu)

        // Tangani klik pada item menu
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_unpublish -> {
                    listener.onMenuClick(position, "Unpublish")
                    true
                }
                R.id.btnHapus -> {
                    listener.onMenuClick(position, "Hapus")
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    // ViewHolder untuk item
    class ViewHolder(val binding: ItemPublishedArtikelBinding) : RecyclerView.ViewHolder(binding.root)

    // Interface untuk mendefinisikan aksi yang diklik
    interface OnItemClickListener {
        fun onJudulClick(position: Int)
        fun onMenuClick(position: Int, action: String) // Aksi dari tiga titik (edit atau delete)
    }
}
