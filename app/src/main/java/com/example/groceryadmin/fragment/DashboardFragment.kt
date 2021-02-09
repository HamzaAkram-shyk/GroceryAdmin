package com.example.groceryadmin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryadmin.Adapter.CategoryAdapter
import com.example.groceryadmin.Adapter.DashboardAdapter
import com.example.groceryadmin.R



class DashboardFragment() : Fragment() {

    private lateinit var LayoutManager: GridLayoutManager
    lateinit var dashRecyclerView: RecyclerView
    lateinit var adapter: DashboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView:View=inflater.inflate(R.layout.fragment_dashboard, container, false)
        dashRecyclerView=mainView.findViewById(R.id.dashboardRecyclerview)
        dashRecyclerView.setHasFixedSize(true)
        LayoutManager = GridLayoutManager(activity,2)
        dashRecyclerView.layoutManager = LayoutManager
        adapter= activity?.let { DashboardAdapter(it) }!!
        dashRecyclerView.adapter=adapter


        return mainView
    }



}