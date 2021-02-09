package com.example.groceryadmin.callbackInterface



interface RepositoryCallback<in H> {
    fun onSuccess(h: H,message:String)
    fun onStart(h:H)
    fun onFailure(h:H,error:String)
}