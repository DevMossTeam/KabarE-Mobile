package com.devmoss.kabare.ui.manajemenartikel.artikeladapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.data.model.StatusPeninjauan
import com.devmoss.kabare.databinding.ItemDalamPeninjauanBinding

class DalamPeninjauanAdapter(
    private val dataList: List<Artikel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DalamPeninjauanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDalamPeninjauanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.tvTanggal.text = data.tanggalPengajuan
        holder.binding.tvTerakhirDiperbarui.text = data.tanggalDiperbarui
        holder.binding.tvJudul.text = data.title
        holder.binding.tvStatusPeninjauan.text = data.statusPeninjauan.status

        // Ubah warna teks berdasarkan status dengan if-else
        if (data.statusPeninjauan == StatusPeninjauan.DIAJUKAN) {
            holder.binding.tvStatusPeninjauan.setTextColor(holder.binding.root.context.getColor(R.color.colorDiajukan))
        } else if (data.statusPeninjauan == StatusPeninjauan.DITOLAK) {
                holder.binding.tvStatusPeninjauan.setTextColor(holder.binding.root.context.getColor(R.color.colorDitolak))
        } else if (data.statusPeninjauan == StatusPeninjauan.REVISI_KECIL) {
                holder.binding.tvStatusPeninjauan.setTextColor(holder.binding.root.context.getColor(R.color.colorRevisiKecil))
        } else if (data.statusPeninjauan == StatusPeninjauan.REVISI_BESAR) {
            holder.binding.tvStatusPeninjauan.setTextColor(holder.binding.root.context.getColor(R.color.colorRevisiBesar))
        }
        
        // Tampilkan tombol berdasarkan status
        if (data.statusPeninjauan == StatusPeninjauan.DIAJUKAN) {
            holder.binding.btnBatalkanPengajuan.visibility = View.VISIBLE
        } else if (data.statusPeninjauan == StatusPeninjauan.DITOLAK ||
            data.statusPeninjauan == StatusPeninjauan.REVISI_KECIL ||
            data.statusPeninjauan == StatusPeninjauan.REVISI_BESAR) {
            holder.binding.btnPindahKeDraf.visibility = View.VISIBLE
        }

        // Menangani klik untuk tombol
        holder.binding.btnBatalkanPengajuan.setOnClickListener {
            listener.onBatalPengajuanClick(position)
        }
        holder.binding.btnPindahKeDraf.setOnClickListener {
            listener.onPindahKeDrafClick(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(val binding: ItemDalamPeninjauanBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onBatalPengajuanClick(position: Int)
        fun onPindahKeDrafClick(position: Int)
    }
}
