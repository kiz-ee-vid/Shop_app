package com.example.lab2.data

import com.example.lab2.domain.IRepository

class RepositoryImpl (private val apiService: ApiService) : IRepository {

    override suspend fun getProducts(): ArrayList<ApiItem>? {
        return apiService.getProducts().body()
    }

    override suspend fun getFinance(): ArrayList<Finance>? {
        return apiService.getFinance().body()
    }
}