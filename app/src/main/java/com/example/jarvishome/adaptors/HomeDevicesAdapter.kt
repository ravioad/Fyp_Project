package com.example.jarvishome.adaptors

import android.content.Context
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
import com.google.firebase.database.FirebaseDatabase

class HomeDevicesAdapter(
    private val context: Context,
    private val list: ArrayList<DeviceModel>
) : RecyclerView.Adapter<HomeDevicesAdapter.MyViewHolder>() {

    private val database = FirebaseDatabase.getInstance().getReference("Controls").child("Lights")
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
        holder.switch?.setOnCheckedChangeListener { _, isChecked ->
            updateValueForGate(isChecked, list[position].name, list[position].pin_number)
        }
    }

    private fun updateValueForGate(isChecked: Boolean, name: String, pin: Int) {
        val device = DeviceModel(name, pin, if (isChecked) 1 else 0)
        database.child(device.name).setValue(device)
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
        }

        fun bind(item: DeviceModel, position: Int) {
            title?.text = item.name
            type?.text = context.resources.getString(R.string.Lights)
            switch?.isChecked = item.value == 1
            if (position % 2 == 0) {
                Glide.with(context).load(R.drawable.lamp).into(icon!!)
            } else {
                Glide.with(context).load(R.drawable.fan).into(icon!!)
            }
        }
    }
}