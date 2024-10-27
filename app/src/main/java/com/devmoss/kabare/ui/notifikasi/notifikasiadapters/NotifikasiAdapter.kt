package com.devmoss.kabare.ui.notifikasi.notifikasiadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.data.model.Notifikasi
import com.devmoss.kabare.databinding.ItemNotifikasiBinding
import com.devmoss.kabare.ui.manajemenartikel.artikeladapters.DrafPenulisAdapter.OnItemClickListener

class NotifikasiAdapter(
    private val notifikasiList: List<Notifikasi>,
    private val listener: OnItemClickListener

) : RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifikasiViewHolder {
        val binding = ItemNotifikasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifikasiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotifikasiViewHolder, position: Int) {
        // Mengakses properti secara langsung tanpa destructuring
        val notifikasi = notifikasiList[position]
        holder.binding.imgNotifikasi.setImageResource(notifikasi.iconResId)
        holder.binding.tvJudulNotifikasi.text = notifikasi.titleNotif
        holder.binding.tvJudulArtikel.text = notifikasi.title
        holder.binding.tvStatusNotifikasi.text = notifikasi.statusNotifikasi //

        holder.binding.tvJudulArtikel.setOnClickListener {
            listener.onJudulArtikelClick(position)

        }
    }

    override fun getItemCount(): Int {
        return notifikasiList.size
    }

    class NotifikasiViewHolder(val binding: ItemNotifikasiBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onJudulArtikelClick(position: Int)
    }
}


