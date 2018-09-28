package com.blockviewer

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blockviewer.R.layout.row_layout
import kotlinx.android.synthetic.main.row_layout.view.*

class BlockAdapter(private val context: Context, private val dataSet: List<Block>,
                           private val listener: OnItemClickListener)
    : RecyclerView.Adapter<BlockAdapter.ViewHolder>() {

    private var selectedBlock: Block? = null

    var selectedLocationIndex: Int
        get() = dataSet.indexOf(selectedBlock)
        set(index) {
            this.selectedBlock = dataSet[index]
        }

    interface OnItemClickListener {
        fun onItemClick(block: Block)
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var title: TextView = itemView.title
        var timestamp: TextView = itemView.timestamp
        var producer: TextView = itemView.producer

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val view = layoutInflater.inflate(R.layout.row_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO add string to resource, ran out of time + compiler issues
        holder.title.text = """Block: ${dataSet[position].block_id}"""
        holder.title.textSize = 18f

        val time = dataSet[position].time_stamp.split("T")
        holder.timestamp.text = """Timestamp: """ + time[0] + """ at: """ + time[1].substring(0, time[1].length - 4)
        holder.timestamp.textSize = 12f


        holder.producer.text = """Producer: """+dataSet[position].producer
        holder.producer.textSize = 12f


        holder.itemView.setOnClickListener {
            listener.onItemClick(dataSet[position])
            selectedBlock = dataSet[position]
            notifyDataSetChanged()
        }
        if (dataSet[position] == selectedBlock) {
            val backgroundColor = ContextCompat.getColor(context, R.color.material_blue_grey_800)
            holder.itemView.setBackgroundColor(backgroundColor)

        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
            holder.title.setTextColor(Color.BLACK)
        }

    }

    override fun getItemCount() = dataSet.size

}