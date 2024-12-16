package com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis.artikeladapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.databinding.ItemDrafPenulisBinding

class DrafPenulisAdapter(
    private var drafList: List<Artikel>,
    private val listener: OnItemClickListener // Menambahkan listener sebagai parameter
) : RecyclerView.Adapter<DrafPenulisAdapter.DrafViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrafViewHolder {
        val binding = ItemDrafPenulisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrafViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrafViewHolder, position: Int) {
        val drafPenulis = drafList[position]

        // Menggunakan binding untuk mengakses tampilan
        holder.binding.tvTanggal.text = drafPenulis.tanggalPengajuan
        holder.binding.tvTerakhirDiperbarui.text = drafPenulis.tanggalDiperbarui
        holder.binding.tvJudul.text = drafPenulis.title

        // Menetapkan listener untuk tombol Hapus
        holder.binding.btnHapus.setOnClickListener {
            listener.onHapusClick(position) // Mengirim posisi item yang diklik ke listener
        }

        // Menetapkan listener untuk tombol Edit
        holder.binding.btnEdit.setOnClickListener {
            listener.onEditClick(position) // Mengirim posisi item yang diklik ke listener
        }

    }

    override fun getItemCount(): Int {
        return drafList.size // Mengembalikan jumlah item dalam list
    }

    // Menambahkan fungsi untuk memperbarui data di adapter
    fun updateData(newDrafList: List<Artikel>) {
        drafList = newDrafList
        notifyDataSetChanged() // Memastikan RecyclerView memperbarui tampilan dengan data baru
    }


    class DrafViewHolder(val binding: ItemDrafPenulisBinding) : RecyclerView.ViewHolder(binding.root)

    // Interface untuk mendefinisikan event klik
    interface OnItemClickListener {
        fun onHapusClick(position: Int) // Event klik untuk tombol Hapus
        fun onEditClick(position: Int)

    }
}
