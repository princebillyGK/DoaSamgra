package com.example.doasamagra.surahplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doasamagra.R


class SurahItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.surah_title)

    companion object {
        fun makeInstance(parent: ViewGroup): SurahItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.surah_list_item, parent, false)
            return SurahItemViewHolder(view)
        }
    }

    fun bind(item: Surah) {
        title.text = item.title
    }

}


internal class SurahItemAdapter : RecyclerView.Adapter<SurahItemViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    var data = listOf<Surah>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahItemViewHolder {
        return SurahItemViewHolder.makeInstance(parent)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SurahItemViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(position)
        })
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}