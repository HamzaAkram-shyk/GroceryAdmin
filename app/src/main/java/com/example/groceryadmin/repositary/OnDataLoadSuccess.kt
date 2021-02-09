package com.example.groceryadmin.repositary

import com.example.groceryadmin.ModelClasses.Category

interface OnDataLoadSuccess {

    fun onSuccess(itemList:List<Category>)
}