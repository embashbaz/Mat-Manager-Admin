package com.example.matatumanageradmin.ui.fleetList

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.matatumanageradmin.data.Bus
import com.example.matatumanageradmin.data.Driver
import com.example.matatumanageradmin.databinding.FleetItemBinding

class FleetListAdapter(onClick: (Any) -> Unit) :
    RecyclerView.Adapter<FleetListAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<Any>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allItems.get(position)

            holder.bind(item)

    }

    override fun getItemCount() = allItems.size


    fun setData(items: ArrayList<Any>) {
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()

    }

    class ViewHolder(fleetItemBinding: FleetItemBinding, onClick: (Any) -> Unit):
                   RecyclerView.ViewHolder(fleetItemBinding.root){

        val binding = fleetItemBinding

        var item : Any? = null

        init {
            binding.root.setOnClickListener {
                onClick(item!!)
            }
        }

        fun bind(item: Any) {
            if (item is Bus){
                this.item = item

                binding.primaryTextFleetItem.text = item.plate
                binding.secondaryTextFleetItem.text = item.carModel
                if(item.picture.isNotEmpty())
                    Glide.with(binding.root).load(item.picture).apply(RequestOptions.circleCropTransform()).into(binding.imageFleetItem)

            }else if (item is Driver){
                this.item = item

                binding.primaryTextFleetItem.text = item.name
                binding.secondaryTextFleetItem.text = item.email
                if(item.pictureLink.isNotEmpty())
                    Glide.with(binding.root).load(item.pictureLink).apply(RequestOptions.circleCropTransform()).into(binding.imageFleetItem)

            }
        }

        companion object {
           fun from(parent: ViewGroup, onClick: (Any ) -> Unit) : ViewHolder{
               val layoutInflater = LayoutInflater.from(parent.context)

                return  ViewHolder(FleetItemBinding.inflate(layoutInflater, parent, false), onClick)
           }
       }
    }


}