package com.example.lab2.data

data class ApiItem(
    var id: Int?,
    var title: String?,
    var price: Double?,
    var description: String?,
    var category: String?,
    var image: String?,
    var rating: Rating?
)

data class Rating(
    var rate: Double?,
    var count: Int?
)