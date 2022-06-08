package com.example.lab2.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab2.data.DB
import com.example.lab2.data.Finance
import com.example.lab2.data.User
import com.example.lab2.domain.ui_model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FavoritesViewModel : ViewModel() {

    private val reference by lazy { DB.getReference() }
    val user = User()
    private var curCurrency: String = "USD"
    var filterProducts = MutableLiveData<ArrayList<Product>?>()

    fun getUser(hash: String) {
        filterProducts.value?.clear()
        reference.child("users").child(hash).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.getValue(User::class.java)!!
                user.list.clear()
                user.list.addAll(temp.list)
                user.login = temp.login
                filterProducts.value?.addAll(temp.list)
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
    }

    fun saveData(product: Product){
        if (!user.list.contains(product)) {
            user.list.add(product)
            reference.child("users").child(user.login.hashCode().toString()).child("list")
                .setValue(user.list)
        }
    }

    fun changeCurrency(currency: String, financeApi: List<Finance>?) {
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