package com.alfrosoft.kwiqshopapp.model

import android.os.Parcelable

/**
 * A data model class for Sold Product with required fields.
 */
@kotlinx.parcelize.Parcelize
data class SoldProduct(
    val userId: String = "",
    val title: String = "",
    val price: String = "",
    val soldQuantity: String = "",
    val image: String = "",
    val orderId: String = "",
    val orderDate: Long = 0L,
    val subTotalAmount: String = "",
    val shippingCharge: String = "",
    val totalAmount: String = "",
    val address: Address = Address(),
    var id: String = "",
) : Parcelable
