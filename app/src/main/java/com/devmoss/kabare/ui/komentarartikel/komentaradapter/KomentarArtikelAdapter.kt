package com.devmoss.kabare.ui.komentarartikel.komentaradapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.ResultGetKomentar
import com.devmoss.kabare.databinding.ItemDialogKomentarArtikelBinding
import com.devmoss.kabare.utils.timeAgo

class KomentarArtikelAdapter(
    private var komentarArtikel: List<ResultGetKomentar>,
    private val listener: OnItemClickListener,
    private val userId: String
) : RecyclerView.Adapter<KomentarArtikelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDialogKomentarArtikelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val komentar = komentarArtikel[position]
        Log.d("KomentarAdapter", "Komentar: ${komentar.teksKomentar}") // Debug log

        // Set nilai view berdasarkan data komentar
        holder.binding.apply {
            tvDescription.text = komentar.teksKomentar
            tvUsername.text = komentar.namaPengguna
            tvTimestamp.text = timeAgo(komentar.dateTimestamp)// Tanggal komentar
        }

        // Menampilkan profilePic jika ada
        if (!komentar.profilePic.isNullOrEmpty()) {
            val imageBytes = Base64.decode(komentar.profilePic, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            Glide.with(holder.itemView.context)
                .load(bitmap)
                .into(holder.binding.ivProfilePicture)
        }


        // Tampilkan atau sembunyikan ivDropdow berdasarkan userId
        if (komentar.userId == userId) {
            holder.binding.ivDropdow.visibility = View.VISIBLE
            holder.binding.ivDropdow.setOnClickListener {
                listener.onHapusKOmentarClick(komentar.id)
            }
        } else {
            holder.binding.ivDropdow.visibility = View.GONE
        }

        // Event klik untuk dropdown menu
        holder.binding.ivDropdow.setOnClickListener { view ->
            showPopupMenu(view, komentar.id) // Kirim ID komentar ke listener
        }
    }

    override fun getItemCount(): Int {
        return komentarArtikel.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newKomentarArtikel: List<ResultGetKomentar>) {
        komentarArtikel = newKomentarArtikel
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemDialogKomentarArtikelBinding) : RecyclerView.ViewHolder(binding.root)

    // Menampilkan popup menu untuk aksi pada komentar
    private fun showPopupMenu(view: View, komentarId: String) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.menu_hapus_komentar)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_hapus -> {
                    // Panggil listener dengan ID komentar
                    listener.onHapusKOmentarClick(komentarId)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    interface OnItemClickListener {
        fun onHapusKOmentarClick(id: String) // Terima ID komentar
    }
}
