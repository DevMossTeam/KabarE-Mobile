package com.devmoss.kabare.ui.komentarartikel.komentaradapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.KomentarArtikel
import com.devmoss.kabare.databinding.ItemDialogKomentarArtikelBinding
import com.devmoss.kabare.utils.timeAgo

class KomentarArtikelAdapter(
    private var komentarArtikel: List<KomentarArtikel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<KomentarArtikelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDialogKomentarArtikelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val komentar = komentarArtikel[position]
        holder.binding.tvDescription.text = komentar.komentar // Perbaikan pada akses komentar
        holder.binding.tvUsername.text = komentar.username
        // Tampilkan waktu yang lalu
        holder.binding.tvTimestamp.text = timeAgo(komentar.timestamp)


        // Menangani klik pada dropdown
        holder.binding.ivDropdow.setOnClickListener { view ->
            showPopupMenu(view, komentar)
        }
    }

    override fun getItemCount(): Int {
        return komentarArtikel.size // Memperbaiki pengembalian item count
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newKomentarArtikel: List<KomentarArtikel>) {
        komentarArtikel = newKomentarArtikel
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemDialogKomentarArtikelBinding) : RecyclerView.ViewHolder(binding.root)

    private fun showPopupMenu(view: View, komentar: KomentarArtikel) {
        // Membuat PopupMenu
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.menu_hapus_komentar) // Pastikan Anda memiliki menu dengan nama ini
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_hapus -> {
                    // Panggil listener untuk menghapus komentar
                    listener.onHapusKOmentarClick(komentar)
                    true
                }
                else -> false
            }
        }
        popupMenu.show() // Tampilkan PopupMenu
    }


    interface OnItemClickListener {
        fun onHapusKOmentarClick(komentar: KomentarArtikel)
    }
}
