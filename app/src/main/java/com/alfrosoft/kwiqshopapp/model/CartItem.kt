package com.alfrosoft.kwiqshopapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem (
    val userId : String = "",
    val productOwnerId :String = "",
    val productId : String = "",
    val title : String = "",
    val price : String = "",
    val image : String = "",
    var cartQuantity : String = "",
    var stockQuantity : String = "",
    var id : String = ""
        ) : Parcelable