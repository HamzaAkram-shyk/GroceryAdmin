package com.example.groceryadmin.callbackInterface

import androidx.lifecycle.LiveData
import com.example.groceryadmin.ModelClasses.Category
import java.util.*

interface CallbackListener {
    fun onSuccess(Response:LiveData<List<Category>>)
    fun onFailuare(message:String)
    fun onStart(message:String)
}