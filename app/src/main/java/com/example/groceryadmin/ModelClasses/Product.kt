package com.example.groceryadmin.ModelClasses

data class Product(
    var name: String = "",
    var detail: String = "",
    var url: String = "",
    var categoryId: String = "",
    var isSale: Boolean = false,
    var percentage: Int = 30
)