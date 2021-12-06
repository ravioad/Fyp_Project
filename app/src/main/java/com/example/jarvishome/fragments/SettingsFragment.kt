package com.example.jarvishome.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.jarvishome.R
import com.example.jarvishome.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(), View.OnClickListener {
    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var mAuth: FirebaseAuth? = null
    private lateinit var database: DatabaseReference
    private lateinit var darkThemeCard: CardView
    private lateinit var ram_pieChart: AnimatedPieView
    private lateinit var disk_pieChart: AnimatedPieView
    private lateinit var ram_percentage: TextView
    private lateinit var disk_percentage: TextView
    private var isOn = false
    private var totalRAM: Double = 0.0
    private var totalDisk: Double = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val screenLayout = inflater.inflate(R.layout.fragment_settings, container, false)
        mAuth = FirebaseAuth.getInstance()
        initView(screenLayout)
        return screenLayout
    }

    private fun initView(screenLayout: View) {
        ram_pieChart = screenLayout.findViewById(R.id.ram_pieChart)
        darkThemeCard = screenLayout.findViewById(R.id.darkThemeCard)
        disk_pieChart = screenLayout.findViewById(R.id.disk_pieChart)
        ram_percentage = screenLayout.findViewById(R.id.ram_percentage)
        disk_percentage = screenLayout.findViewById(R.id.disk_percentage)
        database = FirebaseDatabase.getInstance().getReference("PI")
        darkThemeCard.setOnClickListener(this)
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (x in snapshot.children) {
                    if (x.key == "DISK") {
                        val hash = x.value as HashMap<*, *>
                        val free = (hash["free"] as String).toDouble()
                        val used = (hash["used"] as String).toDouble()
                        totalDisk = (hash["total"] as String).toDouble()
                        disk_percentage.text = "${totalDisk}\nGB"
                        setupDiskPieChart(free, used)
                    }
                    if (x.key == "RAM") {
                        val hash = x.value as HashMap<*, *>
                        val free = (hash["free"] as String).toDouble()
                        val used = (hash["used"] as String).toDouble()
                        totalRAM = (hash["total"] as String).toDouble()
                        ram_percentage.text = "${totalRAM}\nMB"
                        setupRamPieChart(free, used)
                    }
                    if (x.key == "CPU") {
                        val hash = x.value as HashMap<*, *>
                        val temp = (hash["temperature"] as String).toDouble().toInt()
                        cpuTemperature.text = "${temp}\u2103"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupDiskPieChart(free: Double, used: Double) {
        val config = AnimatedPieViewConfig()
        config.addData(
            SimplePieInfo(
                free,
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                "Free space"
            )
        )
        config.addData(
            SimplePieInfo(
                used,
                ContextCompat.getColor(requireContext(), R.color.darkPrimary),
                "Used space"
            )
        )
        config.strokeWidth(resources.getDimension(R.dimen._14sdp).toInt())
        config.duration(2000)
        config.drawText(true)
        config.textSize = resources.getDimension(R.dimen._10ssp)
        config.selectListener(OnPieSelectListener { pieInfo, isFloatUp ->
            Log.e("isFloatUp", isFloatUp.toString())
            if (isFloatUp) {
                val percent = (pieInfo.value / (free + used)) * 100
                disk_percentage.text = "${percent.toInt()}%"
                Log.e("value", pieInfo.value.toString())
            } else {
                disk_percentage.text = "${totalDisk}\nGB"
            }
        })
        disk_pieChart.applyConfig(config)
        disk_pieChart.start()
    }

    @SuppressLint("SetTextI18n")
    private fun setupRamPieChart(free: Double, used: Double) {
        val config = AnimatedPieViewConfig()
        config.addData(
            SimplePieInfo(
                free,
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                "Free space"
            )
        )
        config.addData(
            SimplePieInfo(
                used,
                ContextCompat.getColor(requireContext(), R.color.darkPrimary),
                "Used space"
            )
        )
        config.strokeWidth(resources.getDimension(R.dimen._14sdp).toInt())
        config.duration(2000)
        config.drawText(true)
        config.textSize = resources.getDimension(R.dimen._10ssp)
        config.selectListener(OnPieSelectListener { pieInfo, isFloatUp ->
            Log.e("isFloatUp", isFloatUp.toString())
            if (isFloatUp) {
                val percent = (pieInfo.value / (free + used)) * 100
                ram_percentage.text = "${percent.toInt()}%"
            } else {
                ram_percentage.text = "${totalRAM}\nMB"
            }
        })
        ram_pieChart.applyConfig(config)
        ram_pieChart.start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.darkThemeCard -> {
                mAuth?.signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }
    }
}