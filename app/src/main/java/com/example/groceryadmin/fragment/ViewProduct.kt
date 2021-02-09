package com.example.groceryadmin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryadmin.Adapter.CategoryAdapter
import com.example.groceryadmin.Adapter.ProductAdapter
import com.example.groceryadmin.ModelClasses.Product
import com.example.groceryadmin.R
import com.example.groceryadmin.callbackInterface.RepositoryCallback
import com.example.groceryadmin.viewmodel.CategoryViewModel
import com.example.groceryadmin.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_view_category.view.*
import kotlinx.android.synthetic.main.fragment_view_category.view.loading
import kotlinx.android.synthetic.main.fragment_view_product.view.*

class ViewProduct : Fragment(),RepositoryCallback<LiveData<List<Product>>> {
    private lateinit var rootView: View
    private lateinit var layoutManager: GridLayoutManager
    lateinit var productRecyclerView: RecyclerView
    lateinit var adapter: ProductAdapter
    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_view_product, container, false)
        init()
        return rootView
    }

    private fun init() {
        viewModel=activity?.let {
            ViewModelProvider(it).get(ProductViewModel::class.java)
        }!!
        productRecyclerView = rootView.findViewById(R.id.productRecyclerView)
        productRecyclerView.setHasFixedSize(true)
        layoutManager = GridLayoutManager(activity, 3)
        productRecyclerView.layoutManager = layoutManager
        viewModel.getProductList(this)

    }

    override fun onSuccess(h: LiveData<List<Product>>, message: String) {
        rootView.loading.visibility=View.GONE

        h.observe(this, Observer {
            adapter= ProductAdapter(it as ArrayList<Product>,context!!)
            productRecyclerView.adapter=adapter
            adapter.notifyDataSetChanged()
        })
    }

    override fun onStart(h: LiveData<List<Product>>) {
        rootView.loading.visibility=View.VISIBLE
    }

    override fun onFailure(h: LiveData<List<Product>>, error: String) {
        rootView.loading.visibility=View.GONE
        Toast.makeText(context, "Error $error", Toast.LENGTH_SHORT).show()
    }


}