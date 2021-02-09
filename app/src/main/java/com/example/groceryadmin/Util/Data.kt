 package com.example.groceryadmin.Util

import android.util.Log
import com.example.groceryadmin.ModelClasses.Category
import com.google.firebase.firestore.FirebaseFirestore


class Data {
    companion object {
        fun getCategories():ArrayList<Category> {
            val categoriesItems=ArrayList<Category>()
            val db = FirebaseFirestore.getInstance()
            val categoryRef = db.collection("Category")
            categoryRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val item: Category = document.toObject(Category::class.java)
                        Log.d("Data",item.title)
                        categoriesItems.add(item)
                    }
                }
            }
            Log.d("Size"," "+categoriesItems.size)
        return  categoriesItems
        }
    }
}



