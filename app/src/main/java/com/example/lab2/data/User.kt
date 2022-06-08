package com.example.lab2.data

import com.example.lab2.domain.ui_model.Product

data class User(var login: String, var list: MutableList<Product>) {
    constructor(): this("Not authorized", mutableListOf())
}