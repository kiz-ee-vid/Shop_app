package com.example.lab2.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab2.data.Finance
import com.example.lab2.data.NetworkModule
import com.example.lab2.data.RepositoryImpl
import com.example.lab2.data.User
import com.example.lab2.domain.Mapper
import com.example.lab2.domain.ui_model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    val api = MutableLiveData<ArrayList<Product>?>()
    var filterProducts = MutableLiveData<ArrayList<Product>?>()
    var financeApi: List<Finance>? = mutableListOf()
    private val mapper: Mapper by lazy { Mapper() }
    private var curCurrency: String = "USD"

    fun getData() {
        val repositoryImplProduct =
            RepositoryImpl(NetworkModule.provideApiService(NetworkModule.provideProductRetrofit()))
        val repositoryImplFinance =
            RepositoryImpl(NetworkModule.provideApiService(NetworkModule.provideFinanceRetrofit()))
        CoroutineScope(Dispatchers.Default).launch {
            financeApi = repositoryImplFinance.getFinance()
            val data =
                repositoryImplProduct.getProducts()
                    ?.let { mapper.fromApiProducts(it) } as ArrayList<Product>?
            withContext(Dispatchers.Main) {
                api.value = data
                filterProducts.value = data
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
        }
    }

    fun changeCurrency(currency: String) {
        var index_bel: Int? = null
        var index_cur: Int? = null
        if (financeApi != null)
            for (i in financeApi!!.indices) {
                if (financeApi!![i].Cur_Abbreviation == curCurrency)
                    index_bel = i
                if (financeApi!![i].Cur_Abbreviation == currency)
                    index_cur = i
            }

        filterProducts.value?.forEach { it ->
            it.currency = currency
            val belPrice: Double? = if (index_bel == null)
                it.price!!
            else
                it.price?.times((financeApi?.get(index_bel)?.Cur_OfficialRate!!))
                    ?.div(financeApi?.get(index_bel)?.Cur_Scale!!)

            if (index_cur == null)
                it.price = belPrice
            else
                if (belPrice != null) {
                    it.price =
                        (belPrice / (financeApi?.get(index_cur)?.Cur_OfficialRate!!)).times(
                            financeApi?.get(index_cur)?.Cur_Scale!!
                        )
                }
        }
        curCurrency = currency
    }
}