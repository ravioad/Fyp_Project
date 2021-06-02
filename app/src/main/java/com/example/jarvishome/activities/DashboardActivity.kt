package com.example.jarvishome.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.jarvishome.R
import com.example.jarvishome.adaptors.DashboardBottomNavAdapter
import com.example.jarvishome.changeStatusBarColor
import com.example.jarvishome.fragments.HomeFragment
import com.example.jarvishome.fragments.SettingsFragment
import com.example.jarvishome.models.DeviceModel
import com.example.jarvishome.select
import com.example.jarvishome.unSelect
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    lateinit var bottomNavAdapter: DashboardBottomNavAdapter
    private var isOn = false
    private var isOn2 = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        database = FirebaseDatabase.getInstance().getReference("Controls").child("Lights")
        changeStatusBarColor(false)
//        addData()
//        gateButton.setOnClickListener {
//            updateValueForGate()
//        }
//        exB.setOnClickListener {
//            testing()
//        }

//        setupPieChart()
        setViewPager()
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

    @SuppressLint("InflateParams")
    private fun setViewPager() {
        bottomNavAdapter = DashboardBottomNavAdapter(supportFragmentManager)
        bottomNavAdapter.addFragment(HomeFragment.newInstance())
        bottomNavAdapter.addFragment(SettingsFragment.newInstance())
        main_viewpager.adapter = bottomNavAdapter
        bottomTabLayout.setupWithViewPager(main_viewpager)
        main_viewpager.offscreenPageLimit = 0
        for (i in 0 until bottomNavAdapter.count) {
            val customView = layoutInflater.inflate(R.layout.custom_tab_layout, null)
            val image = customView.findViewById<ImageView>(R.id.icon)
            val params = image.layoutParams
            params.width = resources.getDimension(R.dimen._22sdp).toInt()
            params.height = resources.getDimension(R.dimen._22sdp).toInt()
            image.layoutParams = params
            when (i) {
                0 -> {
                    Glide.with(this).load(R.drawable.home).into(image)
                    image.select(this)
                }
                1 -> {
                    Glide.with(this).load(R.drawable.settings).into(image)
                    image.unSelect(this)
                }
            }
            bottomTabLayout.getTabAt(i)?.customView = customView
        }
        val tabStrip = bottomTabLayout.getChildAt(0)
        if (tabStrip is ViewGroup) {
            for (i in 0 until tabStrip.childCount) {
                val tabView = tabStrip.getChildAt(i)
                val margin = resources.getDimension(R.dimen._40sdp).toInt()
                tabView.setPadding(margin, 0, margin, 0)
            }
            bottomTabLayout.requestLayout()
        }
        addBottomNavListener()
    }

    private fun addBottomNavListener() {
        bottomTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                for (i in 0 until bottomNavAdapter.count) {
                    val img =
                        bottomTabLayout.getTabAt(i)?.customView!!.findViewById<ImageView>(R.id.icon)
                    when (i) {
                        0 -> {
                            if (i == tab?.position) {
                                img.select(this@DashboardActivity)
                                changeStatusBarColor(false)
                            } else {
                                img.unSelect(this@DashboardActivity)
                            }
                        }
                        1 -> {
                            if (i == tab?.position) {
                                changeStatusBarColor()
                                img.select(this@DashboardActivity)
                            } else {
                                img.unSelect(this@DashboardActivity)
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        bottomTabLayout.getTabAt(0)?.select()
//        selectedFragment = bottomNavAdapter.getItem(0) as MyBaseFragment
        main_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
//                selectedFragment = bottomNavAdapter.getItem(position) as MyBaseFragment

            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
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