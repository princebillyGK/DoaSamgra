package com.example.doasamagra.doa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doasamagra.R


class DoaItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var doaTitleText = itemView.findViewById<TextView>(R.id.doa_title)
    private var doaPronInBnText = itemView.findViewById<TextView>(R.id.doa_pron_in_bn)
    private var doaBnText = itemView.findViewById<TextView>(R.id.doa_bn)
    private var doaNoteText = itemView.findViewById<TextView>(R.id.doa_note)

    companion object {
        fun makeInstance(parent: ViewGroup): DoaItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.doa_list_item, parent, false)
            return DoaItemViewHolder(view)
        }
    }

    fun bind(doa: Doa) {
        doaTitleText.text = doa.title
        doaPronInBnText.text = doa.pronInBn
        doa.bn?.let {
            doaBnText.text = doa.bn
            doaBnText.visibility = View.VISIBLE
        }
        doa.note?.let {
            doaNoteText.text = it
            doaNoteText.visibility = View.VISIBLE
        }
    }
}

class DoaItemAdapter : RecyclerView.Adapter<DoaItemViewHolder>() {

    var data = listOf<Doa>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoaItemViewHolder {
        return DoaItemViewHolder.makeInstance(parent)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DoaItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

}

