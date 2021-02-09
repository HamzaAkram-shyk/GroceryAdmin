package com.example.groceryadmin.ModelClasses

data class MenuItem (val titleName:String, val imageResource:Int,val childList:List<String>?,val hasChild:Boolean)
