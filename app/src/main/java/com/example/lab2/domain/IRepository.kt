package com.example.lab2.domain

import com.example.lab2.data.ApiItem
import com.example.lab2.data.Finance

interface IRepository {
    suspend fun getProducts(): ArrayList<ApiItem>?

    suspend fun getFinance(): ArrayList<Finance>?
}