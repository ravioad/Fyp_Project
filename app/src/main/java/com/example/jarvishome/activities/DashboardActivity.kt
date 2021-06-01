package com.example.jarvishome.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jarvishome.R
import com.example.jarvishome.models.DeviceModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    private var isOn = false
    private var isOn2 = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        database = FirebaseDatabase.getInstance().getReference("Controls").child("Lights")
//        addData()
        gateButton.setOnClickListener {
            updateValueForGate()
        }
        exB.setOnClickListener {
            testing()
        }

        setupPieChart()
    }

    private fun setupPieChart() {
        val config = AnimatedPieViewConfig()
        config.addData(
            SimplePieInfo(
                123.toDouble(),
                ContextCompat.getColor(this, R.color.pie1),
                "Free space"
            )
        )
        config.addData(
            SimplePieInfo(
                332.toDouble(),
                ContextCompat.getColor(this, R.color.pie2),
                "Used space"
            )
        )
        config.strokeWidth(resources.getDimension(R.dimen._10sdp).toInt())
        config.duration(2000)
        config.drawText(true)
        config.textSize = resources.getDimension(R.dimen._10ssp)
        config.selectListener(OnPieSelectListener { pieInfo, isFloatUp ->
            val percent = (pieInfo.value / (123.toDouble() + 332.toDouble())) * 100
            percentage.text = "${percent.toInt()}%"
        })
        pieChart.applyConfig(config)
        pieChart.start()
    }

    private fun updateValueForGate() {
        // Channel 8, for pin 21

        val device = if (isOn) {
            isOn = false
            DeviceModel("StoreRoom", 19, 0)
        } else {
            isOn = true
            DeviceModel("StoreRoom", 19, 1)
        }
        database.child(device.name).setValue(device)
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (item in snapshot.children) {
//                    if ()
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@DashboardActivity, error.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//        })
    }

    private fun testing() {
        // Channel 8, for pin 21

        val device = if (isOn2) {
            isOn2 = false
            DeviceModel("Bedroom1", 21, 0)
        } else {
            isOn2 = true
            DeviceModel("Bedroom1", 21, 1)
        }
        database.child(device.name).setValue(device)
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (item in snapshot.children) {
//                    if ()
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@DashboardActivity, error.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//        })
    }

//    private fun addData() {
//        getDataList().forEach {
//            database.child(it.name).setValue(it)
//        }
//    }

    private fun getDataList(): ArrayList<DeviceModel> {
        return arrayListOf(
            DeviceModel("Bedroom1", 21, 0),
            DeviceModel("Kitchen", 20, 0),
            DeviceModel("LivingRoom", 16, 0),
            DeviceModel("Bedroom2", 12, 0),
            DeviceModel("Bathroom", 26, 0),
            DeviceModel("StoreRoom", 19, 0),
            DeviceModel("GuestRoom", 13, 0),
            DeviceModel("Gate", 6, 0)
        )
    }
}