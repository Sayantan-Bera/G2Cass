package com.example.uias

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uias.databinding.RecyclerViewChecklistBinding

class ChecklistAdaptor(private val listener:itemClicked): RecyclerView.Adapter<ChecklistAdaptor.ViewHolder>() {
    var checklist= mutableListOf<CheckListItem>()
    inner class ViewHolder(val binding:RecyclerViewChecklistBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind=RecyclerViewChecklistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val viewholder=ViewHolder(bind)
        bind.root.setOnLongClickListener{
            listener.onItemLongClicked(viewholder.adapterPosition)
            true
        }
        return viewholder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.rvtext.text=checklist[position].checkIt

    }

    override fun getItemCount(): Int {
        return checklist.size
    }
    fun addCheck(checklistitm:CheckListItem){
        if(!checklist.contains(checklistitm)){
            checklist.add(checklistitm)
        }else{
            val index=checklist.indexOf(checklistitm)
            if (checklistitm.isDeleted){
                checklist.removeAt(index)
            }
        }
        notifyDataSetChanged()
    }
}
interface itemClicked{
    fun onItemLongClicked(position: Int)
}