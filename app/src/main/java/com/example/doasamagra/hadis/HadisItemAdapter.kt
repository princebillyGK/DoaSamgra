package com.example.doasamagra.hadis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doasamagra.R

class HadisItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var hadisContentText = itemView.findViewById<TextView>(R.id.hadis_content)
    private var hadisRefernceText = itemView.findViewById<TextView>(R.id.hadis_reference)

    companion object {
        fun makeInstance(parent: ViewGroup): HadisItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.hadis_list_item, parent, false)
            return HadisItemViewHolder(view)
        }
    }

    fun bind(hadis: Hadis) {
        hadisRefernceText.text = hadis.ref
        hadisContentText.text = hadis.content
    }
}

class HadisItemAdapter : RecyclerView.Adapter<HadisItemViewHolder>() {

    var data = listOf<Hadis>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HadisItemViewHolder {
        return HadisItemViewHolder.makeInstance(parent)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HadisItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

}