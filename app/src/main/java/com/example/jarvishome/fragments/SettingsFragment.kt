package com.example.jarvishome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.jarvishome.R
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

    private lateinit var darkThemeCard: CardView
    private lateinit var ram_pieChart: AnimatedPieView
    private lateinit var disk_pieChart: AnimatedPieView
    private lateinit var ram_percentage: TextView
    private lateinit var disk_percentage: TextView
    private var isOn = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val screenLayout = inflater.inflate(R.layout.fragment_settings, container, false)
        initView(screenLayout)
        return screenLayout
    }

    private fun initView(screenLayout: View) {
        ram_pieChart = screenLayout.findViewById(R.id.ram_pieChart)
        darkThemeCard = screenLayout.findViewById(R.id.darkThemeCard)
        disk_pieChart = screenLayout.findViewById(R.id.disk_pieChart)
        ram_percentage = screenLayout.findViewById(R.id.ram_percentage)
        disk_percentage = screenLayout.findViewById(R.id.disk_percentage)
        setupRamPieChart()
        setupDiskPieChart()
        darkThemeCard.setOnClickListener(this)
    }

    private fun setupDiskPieChart() {
        val config = AnimatedPieViewConfig()
        config.addData(
            SimplePieInfo(
                77.toDouble(),
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                "Free space"
            )
        )
        config.addData(
            SimplePieInfo(
                99.toDouble(),
                ContextCompat.getColor(requireContext(), R.color.darkPrimary),
                "Used space"
            )
        )
        config.strokeWidth(resources.getDimension(R.dimen._14sdp).toInt())
        config.duration(2000)
        config.drawText(true)
        config.textSize = resources.getDimension(R.dimen._10ssp)
        config.selectListener(OnPieSelectListener { pieInfo, isFloatUp ->
            val percent = (pieInfo.value / (123.toDouble() + 332.toDouble())) * 100
            disk_percentage.text = "${percent.toInt()}%"
        })
        disk_pieChart.applyConfig(config)
        disk_pieChart.start()
    }

    private fun setupRamPieChart() {
        val config = AnimatedPieViewConfig()
        config.addData(
            SimplePieInfo(
                123.toDouble(),
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                "Free space"
            )
        )
        config.addData(
            SimplePieInfo(
                332.toDouble(),
                ContextCompat.getColor(requireContext(), R.color.darkPrimary),
                "Used space"
            )
        )
        config.strokeWidth(resources.getDimension(R.dimen._14sdp).toInt())
        config.duration(2000)
        config.drawText(true)
        config.textSize = resources.getDimension(R.dimen._10ssp)
        config.selectListener(OnPieSelectListener { pieInfo, isFloatUp ->
            val percent = (pieInfo.value / (123.toDouble() + 332.toDouble())) * 100
            ram_percentage.text = "${percent.toInt()}%"
        })
        ram_pieChart.applyConfig(config)
        ram_pieChart.start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.darkThemeCard -> {
                if (isOn) {
                    darkThemeCard.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.backgroundColor)
                    )
                    dark_theme_text.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    dark_theme_status.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    isOn = false
                } else {
                    isOn = true
                    darkThemeCard.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                    )
                    dark_theme_text.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.backgroundColor))
                    dark_theme_status.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.backgroundColor))
                }

            }
        }
    }
}