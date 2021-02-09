package com.example.groceryadmin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.groceryadmin.ModelClasses.Category
import com.example.groceryadmin.ModelClasses.Product
import com.example.groceryadmin.callbackInterface.RepositoryCallback
import com.example.groceryadmin.repositary.Repo

class ProductViewModel :ViewModel(){

    fun getProductList(callback:RepositoryCallback<LiveData<List<Product>>>){
        Repo.getProducts(callback)
    }
}