package com.example.jarvishome.adaptors

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jarvishome.R
import com.example.jarvishome.models.DeviceModel
import com.google.android.material.switchmaterial.SwitchMaterial

class HomeDevicesAdapter(
    private val context: Context,
    private val list: ArrayList<DeviceModel>
) : RecyclerView.Adapter<HomeDevicesAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeDevicesAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.home_device_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeDevicesAdapter.MyViewHolder, position: Int) {
        holder.bind(list[position], position)
//        holder.itemView.setOnClickListener {
//
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var title: TextView? = null
        var type: TextView? = null
        var icon: ImageView? = null
        var switch: SwitchMaterial? = null

        init {
            title = itemView.findViewById(R.id.home_device_title)
            type = itemView.findViewById(R.id.home_device_type)
            icon = itemView.findViewById(R.id.home_device_image)
            switch = itemView.findViewById(R.id.device_switch)
            switch?.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.e("isChecked", isChecked.toString())
            }
        }

        fun bind(item: DeviceModel, position: Int) {
            title?.text = item.name
            type?.text = context.resources.getString(R.string.Lights)
            if (position % 2 == 0) {
                Glide.with(context).load(R.drawable.lamp).into(icon!!)
            } else {
                Glide.with(context).load(R.drawable.fan).into(icon!!)
            }
        }
    }
}