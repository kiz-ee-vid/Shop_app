package com.example.lab2.domain

import com.example.lab2.data.ApiItem
import com.example.lab2.data.Finance
import com.example.lab2.domain.ui_model.Product

class Mapper {
    fun fromApiProducts(apiItems: ArrayList<ApiItem>) = apiItems.map { it.toProduct() }

    private fun ApiItem.toProduct(): Product {
        return Product(
            title = this.title,
            description = this.description,
            category = this.category,
            image = this.image,
            price = this.price
            )
    }
}