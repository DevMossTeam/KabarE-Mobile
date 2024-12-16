package com.devmoss.kabare.ui.home.populer.populeradapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmoss.kabare.data.model.Tag
import com.devmoss.kabare.databinding.ItemTagPopulerBinding

class TagPopulerAdapter(
    private var tagPopulerList: List<Tag>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TagPopulerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       val binding = ItemTagPopulerBinding.inflate(LayoutInflater.from(parent.context),
           parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder : ViewHolder, position: Int) {
        val tagPopuler = tagPopulerList[position]
        holder.binding.tvTag.text = tagPopuler.namaTag
        holder.binding.jumlahTag.text = tagPopuler.jumlahTag.toString()
        holder.binding.kontenClik.setOnClickListener {
            listener.onTagPopulerClick(tagPopuler)
        }
    }
    override fun getItemCount(): Int {
        return tagPopulerList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTagPopulerList(newTagPopuler: List<Tag>) {
        tagPopulerList = newTagPopuler
        notifyDataSetChanged()
    }
    class ViewHolder(val binding: ItemTagPopulerBinding) : RecyclerView.ViewHolder(binding.root)
    interface OnItemClickListener{
        fun onTagPopulerClick(tagPopuler: Tag)
    }
}