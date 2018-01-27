package com.dinizdesenvolve.planejar.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dinizdesenvolve.planejar.R
import com.dinizdesenvolve.planejar.domain.Summary

/**
 * Created by crist on 25/01/2018.
 */
class RecyclerSummaryAdapter(var data:List<Summary>) : RecyclerView.Adapter<RecyclerSummaryAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var cell: View? = null
        if (parent != null) {
            this.context = parent.context
            var inflater = LayoutInflater.from(parent.context)
            cell = inflater.inflate(android.R.layout.simple_spinner_dropdown_item,parent,false)
        }
        return ViewHolder(cell)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder != null){
            holder.text1.text = data.get(position).yearMonth
        }
    }

    override fun getItemCount(): Int {

        return data.count()
    }


    class ViewHolder : RecyclerView.ViewHolder {

        lateinit var text1: TextView

        constructor(itemView: View?) : super(itemView) {
            if (itemView != null){
                //this.text1 = itemView.findViewById(R.id.text1)
            }
        }
    }

}