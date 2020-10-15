package com.example.doasamagra.surahplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doasamagra.R


class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.surah_title)

    companion object {
        fun makeInstance(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.surah_list_item, parent, false)
            return ViewHolder(view)
        }
    }

    fun bind(item: Surah) {
        title.text = item.title
    }
}

internal class SurahItemAdapter : RecyclerView.Adapter<ViewHolder>() {

    var data = listOf<Surah>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.makeInstance(parent)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}