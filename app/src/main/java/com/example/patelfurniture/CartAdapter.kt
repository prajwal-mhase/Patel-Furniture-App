package com.example.patelfurniture

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.patelfurniture.databinding.ItemCartBinding

class CartAdapter(
    private val cartItemList: MutableList<CartItem>,   // List of cart items
    private val context: Context,
    private val onQuantityChanged: () -> Unit,         // Callback to notify CartActivity of quantity changes
    private val onItemRemoved: (Int) -> Unit           // Callback to notify CartActivity when an item is removed
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // Refresh the list to always have the latest data
    private val uniqueCartItemList = cartItemList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        // Inflate the item cart layout using view binding
        val binding = ItemCartBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = uniqueCartItemList[position]

        // Bind data to views
        holder.binding.productNameTextView.text = cartItem.name
        holder.binding.productCategoryTextView.text = cartItem.category
        holder.binding.productSizeTextView.text = cartItem.size
        holder.binding.productPriceTextView.text = "₹ %.2f".format(cartItem.price)
        holder.binding.productQuantityTextView.text = cartItem.quantity.toString()

        // Listener for increasing quantity
        holder.binding.imageView2.setOnClickListener {
            cartItem.quantity++  // Increment the quantity
            holder.binding.productQuantityTextView.text = cartItem.quantity.toString()  // Update the quantity display
            onQuantityChanged()  // Notify CartActivity to update the total price
        }

        // Listener for decreasing quantity (minus button)
        holder.binding.imageView.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--  // Decrease the quantity
                holder.binding.productQuantityTextView.text = cartItem.quantity.toString()  // Update the quantity display
                onQuantityChanged()  // Notify CartActivity to update the total price
            } else {
                // When quantity reaches 1 and the minus button is pressed, remove the item from the list
                removeItem(position)  // Remove item completely
            }
        }

        // Set up the "Remove" button listener
        holder.binding.removeItemButton.setOnClickListener {
            removeItem(position)  // Use a safe remove function to handle removal
        }
    }

    override fun getItemCount(): Int {
        return uniqueCartItemList.size  // Return the total number of unique items in the cart
    }

    // Safe removal of an item and notifying the adapter
    private fun removeItem(position: Int) {
        try {
            if (position >= 0 && position < uniqueCartItemList.size) {
                val cartItemToRemove = uniqueCartItemList[position]
                // Remove the item from both lists
                cartItemList.remove(cartItemToRemove)
                uniqueCartItemList.remove(cartItemToRemove)

                // Notify RecyclerView about the item removal
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, uniqueCartItemList.size)

                // Notify CartActivity for further UI updates
                onItemRemoved(position)

                // Also update SharedPreferences by removing the item
                CartManager.removeFromCart(context, cartItemToRemove.id)
            }
        } catch (e: Exception) {
            Log.e("CartAdapter", "Error removing item at position $position", e)
        }
    }

    // ViewHolder class to hold the item views
    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)
}
