package com.example.groceryadmin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groceryadmin.ModelClasses.Category
import com.example.groceryadmin.R
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.dashboard_item.view.*

class DashboardAdapter(private val context: Context): RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val countText: TextView =itemView.count_textview
        val itemIcon: ImageView =itemView.icon
        val tagText:TextView=itemView.tag_textview

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item,parent,false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       setCardValue(position,holder)
    }


    private fun setCardValue(position: Int,holder: ViewHolder){
        when(position){
           0->{
              holder.itemIcon.setImageResource(R.drawable.list_icon)
              holder.countText.text="30"
              holder.tagText.text="Total Category"
           }
            1->{
                holder.itemIcon.setImageResource(R.drawable.list_icon)
                holder.countText.text="10"
                holder.tagText.text="Total Product"
            }
            2->{
                holder.itemIcon.setImageResource(R.drawable.user_icon)
                holder.countText.text="10"
                holder.tagText.text="Users"
            }
            3->{
                holder.itemIcon.setImageResource(R.drawable.feedback_icon)
                holder.countText.text="20"
                holder.tagText.text="Feedback"
            }

            4->{
                holder.itemIcon.setImageResource(R.drawable.dashboard_order_icon)
                holder.countText.text="20"
                holder.tagText.text="Total Orders"
            }
            5->{
                holder.itemIcon.setImageResource(R.drawable.sale_icon)
                holder.countText.text="30"
                holder.tagText.text="Sale Items"
            }
        }
    }


}