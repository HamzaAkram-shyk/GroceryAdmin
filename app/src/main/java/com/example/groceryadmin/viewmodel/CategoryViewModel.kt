package com.example.groceryadmin.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groceryadmin.ModelClasses.Category
import com.example.groceryadmin.callbackInterface.CallbackListener
import com.example.groceryadmin.callbackInterface.RepositoryCallback


import com.example.groceryadmin.repositary.Repo

class CategoryViewModel : ViewModel() {
    lateinit var mutableCategoryList: MutableLiveData<List<Category>>
    lateinit var mutableBoolean: MutableLiveData<Boolean>
    private lateinit var listener: CallbackListener

    fun init(listener: CallbackListener) {
     this.listener=listener
    }

//     fun getMutableCategoryList(): LiveData<List<Category>> {
//        mutableCategoryList = Repo.getCategoryList()
//        return mutableCategoryList
//    }

    fun getCategory(){
        Repo.getCategoryList(listener)
    }
    fun _removeCategory(categoryId:String,listener: RepositoryCallback<LiveData<Boolean>>){
      Repo.removeCategory(categoryId,listener)
    }

    fun getMutableBoolean(category: Category,uri: Uri):LiveData<Boolean>{
        mutableBoolean=Repo.getCategoryUploaded(category,uri)
        return mutableBoolean
    }

}