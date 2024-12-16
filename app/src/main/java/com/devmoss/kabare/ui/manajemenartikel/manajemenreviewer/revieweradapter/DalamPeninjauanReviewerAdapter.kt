package com.devmoss.kabare.ui.manajemenartikel.manajemenreviewer.revieweradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.databinding.ItemDalamPeninjauanReviewerBinding

class DalamPeninjauanReviewerAdapter(
    private val dataList: List<Artikel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DalamPeninjauanReviewerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDalamPeninjauanReviewerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.tvTerakhirDIajukan.text = data.tanggalPengajuan
        holder.binding.tvJudul.text = data.title

        holder.binding.btnReview.setOnClickListener(){
            listener.onReviewClick(position)
        }

    }
    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(val binding: ItemDalamPeninjauanReviewerBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onReviewClick(position: Int)
    }
}
