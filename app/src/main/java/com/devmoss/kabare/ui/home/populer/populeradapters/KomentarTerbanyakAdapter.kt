package com.devmoss.kabare.ui.home.populer.populeradapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.databinding.ItemKomentarTerbanyakBinding

class KomentarTerbanyakAdapter(
    private var komentarTerbanyak: List<ListBerita>,
    private val listener:OnItemClickListener,

): RecyclerView.Adapter<KomentarTerbanyakAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemKomentarTerbanyakBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val komentarTerbanyak = komentarTerbanyak[position]
        holder.binding.tvJudul.text =komentarTerbanyak.judul
        holder.binding.jumlahKomentar.text =komentarTerbanyak.jumlahKomentar.toString()
        holder.binding.item.setOnClickListener {
            listener.onBeritaKomentarTerbanyakClick(komentarTerbanyak)
        }
    }
    override fun getItemCount(): Int {
        return komentarTerbanyak.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateKomentarTerbanyakList(newKomentarTerbanyak: List<ListBerita>) {
        komentarTerbanyak = newKomentarTerbanyak
        notifyDataSetChanged()
    }
    class ViewHolder(val binding: ItemKomentarTerbanyakBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBeritaKomentarTerbanyakClick(komentarTerbanyak: ListBerita)
    }
}