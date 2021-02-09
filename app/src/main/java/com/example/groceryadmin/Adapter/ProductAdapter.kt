package com.example.groceryadmin.Adapter

import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groceryadmin.ModelClasses.Product
import com.example.groceryadmin.R
import kotlinx.android.synthetic.main.product_layout.view.*

class ProductAdapter(private val products: ArrayList<Product>, private val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productTitle: TextView = itemView.title_textview
        val description: TextView = itemView.desc_textview
        val productImage: ImageView = itemView.product_img
        val moreOption: ImageButton = itemView.menuIcon

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_layout, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.description.text = product.percentage.toString()
        holder.productTitle.text=product.name
        Glide
            .with(context)
            .load(product.url)
            .centerCrop()
            .placeholder(R.drawable.loading_icon)
            .into(holder.productImage);
        holder.moreOption.setOnClickListener {
            // Here popup Menu Appear ..

            popupMenu(it,product)
        }
    }

    private fun popupMenu(view: View, product: Product) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menu.add(Menu.NONE, 1, 1, "View")
        popupMenu.menu.add(Menu.NONE, 2, 2, "Edit")
        popupMenu.menu.add(Menu.NONE, 3, 3, "Apply Sale")
        popupMenu.menu.add(Menu.NONE, 4, 4, "Delete")
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                1 ->{
                    Toast.makeText(context, "View: ${product.name}", Toast.LENGTH_SHORT).show()
                    popupMenu.dismiss()
                }


                2 ->
                    Toast.makeText(context, "Edit:  ${product.name}", Toast.LENGTH_SHORT).show()
                3 ->
                    Toast.makeText(context, "Sale:  ${product.name}", Toast.LENGTH_SHORT).show()
                4 ->
                    Toast.makeText(context, "Delete... ${product.name}", Toast.LENGTH_SHORT).show()
            }
            true
        })
        popupMenu.show()

    }

}

