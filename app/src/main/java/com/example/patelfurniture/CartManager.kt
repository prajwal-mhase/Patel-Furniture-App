package com.example.patelfurniture

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartManager private constructor() {

    companion object {

        private var instance: CartManager? = null
        private const val PREFS_NAME = "cart_prefs"
        private const val CART_KEY = "cart_items"

        // Get Singleton Instance of CartManager
        fun getInstance(context: Context): CartManager {
            if (instance == null) {
                instance = CartManager()
            }
            return instance!!
        }

        // Save Cart Items to SharedPreferences
        fun saveCartItems(context: Context, cartItems: List<CartItem>) {  // Change private to public (fun -> fun)
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(cartItems)
            editor.putString(CART_KEY, json)
            editor.apply()
        }

        // Get Cart Items from SharedPreferences
        fun getCartItems(context: Context): List<CartItem> {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(CART_KEY, null)
            val type = object : TypeToken<List<CartItem>>() {}.type
            return if (json != null) {
                gson.fromJson(json, type)
            } else {
                emptyList()
            }
        }

        // Add Item to Cart (Updated)
        fun addToCart(context: Context, item: CartItem) {
            val currentItems = getCartItems(context).toMutableList()
            val existingItemIndex = currentItems.indexOfFirst { it.id == item.id }

            if (existingItemIndex != -1) {
                // Item already exists, so update the quantity
                val existingItem = currentItems[existingItemIndex]
                existingItem.quantity += item.quantity
            } else {
                // Add new item to cart
                currentItems.add(item)
            }
            saveCartItems(context, currentItems)  // Save updated cart list
        }

        // Remove Item from Cart
        fun removeFromCart(context: Context, itemId: Int) {
            val currentItems = getCartItems(context).toMutableList()
            val itemToRemove = currentItems.firstOrNull { it.id == itemId }

            if (itemToRemove != null) {
                currentItems.remove(itemToRemove)  // Remove the item from the list
                saveCartItems(context, currentItems)  // Save updated cart list
                Log.d("CartManager", "Item removed: $itemId")
            } else {
                Log.d("CartManager", "Item not found: $itemId")
            }
        }

        // Clear all items from the Cart
        fun clearCart(context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove(CART_KEY)  // Remove the cart data
            editor.apply()
        }
    }
}
