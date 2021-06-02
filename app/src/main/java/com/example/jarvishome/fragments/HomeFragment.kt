package com.example.jarvishome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jarvishome.R


class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

    }
}