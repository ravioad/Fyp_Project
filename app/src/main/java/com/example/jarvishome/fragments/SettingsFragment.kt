package com.example.jarvishome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jarvishome.R

class SettingsFragment : Fragment() {
    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

    }

}