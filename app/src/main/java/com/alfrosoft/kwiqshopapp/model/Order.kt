package com.alfrosoft.kwiqshopapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order (
    val userId : String = "",
    val items : ArrayList<CartItem> = ArrayList(),
    val address: Address = Address(),
    val title : String = "",
    val image : String = "",
    val subTotalAmount : String = "",
    val shippingCharge : String = "",
    val totalAmount : String = "",
    val orderDateTime : Long = 0L,
    var id : String = ""
        ) : Parcelable