package com.devmoss.kabare.ui.notifikasi.notifikasiadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.Notifikasi
import com.devmoss.kabare.databinding.ItemNotifikasiBinding
import com.devmoss.kabare.ui.home.terbaru.terbaruadapters.BeritaTerkiniAdapter.OnItemClickListener
import com.devmoss.kabare.utils.timeAgo

class NotifikasiAdapter(
    private var beritaTerkini: List<ListBerita>,
    private val listener: OnItemClickListener,
    ) : RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifikasiViewHolder {
        val binding = ItemNotifikasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifikasiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotifikasiViewHolder, position: Int) {
        val beritaTerkini = beritaTerkini[position]
        holder.binding.timestamp.text = timeAgo(beritaTerkini.tanggalDiterbitkan)

        holder.binding.tvJudulNotifikasi.text = beritaTerkini.judul

        holder.binding.notifikasiItem.setOnClickListener {
            listener.onJudulArtikelClick(beritaTerkini)
        }
    }

    override fun getItemCount(): Int {
        return beritaTerkini.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNotifikasiList(newArticles: List<ListBerita>) {
        beritaTerkini= newArticles
        notifyDataSetChanged()
    }

    class NotifikasiViewHolder(val binding: ItemNotifikasiBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onJudulArtikelClick(beritaTerkini: ListBerita)
    }
}


