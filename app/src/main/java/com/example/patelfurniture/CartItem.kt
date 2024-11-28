package com.example.patelfurniture

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val id: Int,  // Unique identifier for the cart item
    val name: String,
    val category: String,
    val size: String,
    val price: Double,
    var quantity: Int
) : Parcelable
