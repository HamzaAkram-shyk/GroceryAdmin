package com.example.groceryadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryadmin.Adapter.SideMenuAdapter
import com.example.groceryadmin.ModelClasses.MenuItem
import com.example.groceryadmin.Util.Data
import com.example.groceryadmin.fragment.*

class MainActivity : AppCompatActivity(), SideMenuAdapter.OnItemClickListener {

    val button:Button?=null
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var menuRecyclerView:RecyclerView
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R .layout.activity_main)
         menuRecyclerView=findViewById(R.id.sideMenu_recycleView)
         var menuAdapter=SideMenuAdapter(getMenuItems(),this,applicationContext)
         menuRecyclerView.setHasFixedSize(true)
         linearLayoutManager = LinearLayoutManager(this)
         menuRecyclerView.layoutManager = linearLayoutManager
         menuRecyclerView.adapter=menuAdapter
         updateFragment(AddCategory())

    }

 private fun getMenuItems():ArrayList<MenuItem>{
     var menuItems=ArrayList<MenuItem>()
     val productList=ArrayList<String>()
     productList.add("View Product")
     productList.add("Pending Order")
     productList.add("Completed Order")
     val categoryList=ArrayList<String>()
     categoryList.add("View Category")
     categoryList.add("Pending Order")
     val orderList=ArrayList<String>()
     orderList.add("Pending Order")
     orderList.add("Completed Order")
     orderList.add("Pending Order")
     orderList.add("Completed Order")
     menuItems.add(MenuItem("Dashboard",R.drawable.dashboard_icon,null,false))
     menuItems.add(MenuItem("Add Product",R.drawable.product_icon,productList,true))
     menuItems.add(MenuItem("Add Category",R.drawable.category_icon,categoryList,true))
     menuItems.add(MenuItem("Orders",R.drawable.pending_order_icon,orderList,true))
     menuItems.add(MenuItem("Send Notify",R.drawable.notification_icon,null,false))
     menuItems.add(MenuItem("Setting",R.drawable.setting_icon,null,false))
     menuItems.add(MenuItem("Logout",R.drawable.logout_icon,orderList,true))
     return menuItems
 }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position Clicked", Toast.LENGTH_SHORT).show()
        updateScreen(position)
    }


     fun updateScreen(position: Int){
        // This function is responsible to change UI screen

         when(position){
             0->{
                 updateFragment(AddCategory())
             }
             1->{
                 updateFragment(ViewCategory())
             }
             2->{
                 updateFragment(AddProduct())

             }
             3->{
               updateFragment(ViewProduct())
             }
             4->{

             }
             5->{

             }
             6->{

             }
             7->{

             }
             8->{

             }
             9->{

             }
             10->{

             }
         }
    }

    private fun updateFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



}