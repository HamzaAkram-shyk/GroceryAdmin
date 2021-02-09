package com.example.groceryadmin.Adapter

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryadmin.ModelClasses.MenuItem
import com.example.groceryadmin.R
import kotlinx.android.synthetic.main.menu_item_layout.view.*


class SideMenuAdapter(
    private val menuItems: ArrayList<MenuItem>,
    private val listener: OnItemClickListener,
    private val context: Context
) : RecyclerView.Adapter<SideMenuAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val menuTitle: TextView = itemView.titleTextView
        val menuIcon: ImageView = itemView.iconImageView
        val dropIcon: ImageButton = itemView.dropBtn
        val childs: RecyclerView = itemView.childList

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.menu_item_layout, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = menuItems[position]
        if (item.hasChild) {
            holder.menuTitle.text = item.titleName
            holder.menuIcon.setImageResource(item.imageResource)
            holder.childs.setHasFixedSize(true)
            val layout: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
            holder.childs.layoutManager = layout
            val adapter = InnerAdapter(item.childList as ArrayList<String>, context)
            holder.childs.adapter = adapter
            holder.childs.visibility=View.GONE
            holder.dropIcon.setOnClickListener {
                when (holder.childs.visibility == View.GONE) {
                    true -> {
                        holder.childs.visibility=View.VISIBLE
                        animateView(holder.childs)
                    }
                    false -> {
                        animateView(holder.childs)
                        holder.childs.visibility=View.GONE
                    }
                }
            }
        } else {
            holder.menuTitle.text = item.titleName
            holder.menuIcon.setImageResource(item.imageResource)
        }


    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private fun animateView(view: View) {
        val animate = TranslateAnimation(0F, 0F, view.height.toFloat(), 0F)
        animate.duration = 300
        animate.fillAfter = true
        view.startAnimation(animate)
    }


}


