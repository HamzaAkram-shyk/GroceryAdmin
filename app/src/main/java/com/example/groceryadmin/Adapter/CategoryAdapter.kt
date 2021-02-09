package com.example.groceryadmin.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groceryadmin.ModelClasses.Category
import com.example.groceryadmin.R
import com.example.groceryadmin.fragment.OnKeyRemove
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.menu_item_layout.view.*

class CategoryAdapter(private val categories:ArrayList<Category>,private val context:Context,private val listener:OnKeyRemove): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val categoryText: TextView =itemView.text_category
        val categoryImage: ImageView =itemView.image_category
         val removeIcon:ImageButton=itemView.removeBtn


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category=categories[position]
        holder.categoryText.text=category.title
        Glide
            .with(context)
            .load(category.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.loading_icon)
            .into(holder.categoryImage);

        holder.removeIcon.setOnClickListener {

            listener.onRemoveCategory(category.categoryId,category.title)
        }
    }


}