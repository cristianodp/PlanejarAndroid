package com.dinizdesenvolve.planejar.adapters

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dinizdesenvolve.planejar.R
import com.dinizdesenvolve.planejar.domain.Moviment
import com.dinizdesenvolve.planejar.domain.TypeMovto
import com.dinizdesenvolve.planejar.utils.getYearMonth
import java.time.Month




@RequiresApi(Build.VERSION_CODES.O)
/**
 * Created by crist on 06/01/2018.
 */
class RecyclerMovtAdapter(var data:List<Moviment>) : RecyclerView.Adapter<RecyclerMovtAdapter.ViewHolder>() {

    lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var cell:View? = null
        if (parent != null) {
            this.context = parent.context
            var inflater = LayoutInflater.from(parent.context)
            cell = inflater.inflate(R.layout.list_item_moviment,parent,false)
        }
        return ViewHolder(cell)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder != null){
            var ic = R.drawable.ic_down
            if (data.get(position).type == TypeMovto.EXPENCIE){
                ic =  R.drawable.ic_up
            }
            holder.imageViewIcon.setImageDrawable(getDrawable(this.context, ic))
            holder.textViewDescrition.text = data.get(position).description
            holder.textViewValue.text = data.get(position).value.toString()
        }
    }

    override fun getItemCount(): Int {

        return data.count()
    }


    class ViewHolder : RecyclerView.ViewHolder {

        lateinit var imageViewIcon:ImageView
        lateinit var textViewDescrition: TextView
        lateinit var textViewValue : TextView

        constructor(itemView: View?) : super(itemView) {
            if (itemView != null){
                this.imageViewIcon = itemView.findViewById(R.id.imageViewIcon)
                this.textViewValue = itemView.findViewById(R.id.textViewMovValue)
                this.textViewDescrition = itemView.findViewById(R.id.textViewMovDescrition)
            }
        }
    }

}