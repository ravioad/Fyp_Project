package com.example.jarvishome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jarvishome.R
import com.example.jarvishome.adaptors.HomeDevicesAdapter
import com.example.jarvishome.models.DeviceModel


class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var homeRecyclerView: RecyclerView
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
        homeRecyclerView = screenLayout.findViewById(R.id.homeRecyclerView)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = HomeDevicesAdapter(requireContext(), getDataList())
        }
    }

    private fun getDataList(): ArrayList<DeviceModel> {
        val list = arrayListOf(
            DeviceModel("Bedroom1", 21, 0),
            DeviceModel("Kitchen", 20, 0),
            DeviceModel("LivingRoom", 16, 0),
            DeviceModel("Bedroom2", 12, 0),
            DeviceModel("Bathroom", 26, 0),
            DeviceModel("StoreRoom", 19, 0),
            DeviceModel("GuestRoom", 13, 0),
            DeviceModel("Gate", 6, 0)
        )
        list.sortBy { item -> item.name }
        return list
    }
}