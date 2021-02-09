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
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryadmin.Adapter.CategoryAdapter
import com.example.groceryadmin.ModelClasses.Category
import com.example.groceryadmin.R
import com.example.groceryadmin.callbackInterface.CallbackListener
import com.example.groceryadmin.callbackInterface.RepositoryCallback

import com.example.groceryadmin.viewmodel.CategoryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_view_category.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

interface OnKeyRemove {
    fun onRemoveCategory(categoryId: String, categoryName: String)
}

class ViewCategory : Fragment(), CallbackListener, OnKeyRemove,
    RepositoryCallback<LiveData<Boolean>> {
    private lateinit var layoutManager: GridLayoutManager
    lateinit var menuRecyclerView: RecyclerView
    lateinit var adapter: CategoryAdapter
    private lateinit var viewModel: CategoryViewModel
    private lateinit var mainView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainView = inflater.inflate(R.layout.fragment_view_category, container, false)
        init()

        return mainView

    }

    private fun init() {
        menuRecyclerView = mainView.findViewById(R.id.categoryRecyclerView)
        menuRecyclerView.setHasFixedSize(true)
        layoutManager = GridLayoutManager(activity, 3)
        viewModel = activity?.let {
            ViewModelProvider(it).get(CategoryViewModel::class.java)
        }!!
        viewModel.init(this)
        menuRecyclerView.layoutManager = layoutManager
        viewModel.getCategory()
    }

    private fun showDialog(categoryName: String, categoryId: String) {
        MaterialAlertDialogBuilder(context).setTitle("Alert Note !")
            .setMessage(
                "If you remove this Category then its respected Products will remove " +
                        "Automatically are you sure to remove $categoryName ?"
            )
            .setIcon(R.drawable.alert_icon)

            .setCancelable(false)
            .setNegativeButton("Close") { dialog, it ->
                dialog.cancel()
            }
            .setPositiveButton("Remove") { dialog, it ->
                viewModel._removeCategory(categoryId, this)
                dialog.dismiss()
            }
            .show()

    }

    override fun onSuccess(Response: LiveData<List<Category>>) {
        mainView.loading.visibility = View.GONE
        Response.observe(this, Observer {
            adapter = CategoryAdapter(it as ArrayList<Category>, context!!, this)
            menuRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()

        })

    }

    override fun onFailuare(message: String) {
        Toast.makeText(context, "onFailure....", Toast.LENGTH_LONG).show()
        mainView.loading.visibility = View.GONE
        Toast.makeText(context, "Error $message", Toast.LENGTH_LONG).show()
    }

    override fun onStart(message: String) {

        mainView.loading.visibility = View.VISIBLE
    }

    override fun onRemoveCategory(categoryId: String, categoryName: String) {
        // Here we recieved categoryId and then we will show alert to
        // our user if they want to remove product or just cancel our dialog ...
        showDialog(categoryName, categoryId)
        Toast.makeText(context, "get: $categoryId", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(h: LiveData<Boolean>, message: String) {

        h.observe(this, Observer {

            //viewModel.getCategory()
            mainView.loading.visibility = View.GONE
            Toast.makeText(context, "Delete succesfully.......", Toast.LENGTH_SHORT).show()


        })


    }

    override fun onStart(h: LiveData<Boolean>) {
        mainView.loading.visibility = View.VISIBLE
    }

    override fun onFailure(h: LiveData<Boolean>, error: String) {
        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
    }


}