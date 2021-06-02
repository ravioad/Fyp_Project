package com.example.jarvishome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jarvishome.R
import com.example.jarvishome.adaptors.HomeDevicesAdapter
import com.example.jarvishome.makeGone
import com.example.jarvishome.makeVisible
import com.example.jarvishome.models.DeviceModel
import com.example.jarvishome.showToastShort
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    private lateinit var progressBar: ProgressBar
    private lateinit var database: DatabaseReference
    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DeviceModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val screenLayout = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(screenLayout)
        return screenLayout
    }

    private fun initViews(screenLayout: View) {
        dataList = ArrayList()
        homeRecyclerView = screenLayout.findViewById(R.id.homeRecyclerView)
        progressBar = screenLayout.findViewById(R.id.progressBar)
        database = FirebaseDatabase.getInstance().getReference("Controls").child("Lights")
        showLoading()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (dataList.size == 0) {
                    for (x in snapshot.children) {
                        val hash = x.value as HashMap<*, *>
                        dataList.add(
                            DeviceModel(
                                hash["name"] as String,
                                (hash["pin_number"] as Long).toInt(),
                                (hash["value"] as Long).toInt()
                            )
                        )
                    }
                    hideLoading()
                    setupRecyclerView(dataList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                hideLoading()
                error.message.showToastShort(requireContext())
            }

        })
    }

    private fun setupRecyclerView(list: ArrayList<DeviceModel>) {
        homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = HomeDevicesAdapter(requireContext(), list)
        }
    }

    private fun showLoading() {
        progressBar.makeVisible()
        homeRecyclerView.makeGone()
    }

    private fun hideLoading() {
        progressBar.makeGone()
        homeRecyclerView.makeVisible()
    }
//    private fun getDataList(): ArrayList<DeviceModel> {
//        val list = arrayListOf(
//            DeviceModel("Bedroom1", 21, 0),
//            DeviceModel("Kitchen", 20, 0),
//            DeviceModel("LivingRoom", 16, 0),
//            DeviceModel("Bedroom2", 12, 0),
//            DeviceModel("Bathroom", 26, 0),
//            DeviceModel("StoreRoom", 19, 0),
//            DeviceModel("GuestRoom", 13, 0),
//            DeviceModel("Gate", 6, 0)
//        )
//        list.sortBy { item -> item.name }
//        return list
//    }
}