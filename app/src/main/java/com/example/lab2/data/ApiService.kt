package com.example.lab2.data

import com.example.lab2.domain.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(Constants.API_PRODUCTS)
    suspend fun getProducts(): Response<ArrayList<ApiItem>>

    @GET(Constants.API_FINANCE)
    suspend fun getFinance(): Response<ArrayList<Finance>>
}