package com.example.groceryadmin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryadmin.R
import kotlinx.android.synthetic.main.child_item_view.view.*


class InnerAdapter(private val items:ArrayList<String>, private val context:Context): RecyclerView.Adapter<InnerAdapter.ViewHolder>() {


     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemText: TextView =itemView.title_Textview

     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.child_item_view,parent,false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title=items[position]
        holder.itemText.text=title

    }





}