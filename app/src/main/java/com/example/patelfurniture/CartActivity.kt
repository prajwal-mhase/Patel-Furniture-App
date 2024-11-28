package com.example.patelfurniture

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.patelfurniture.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartItems: MutableList<CartItem>  // Mutable list to update cart items

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with LinearLayoutManager
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Get cart items from CartManager
        cartItems = CartManager.getCartItems(this).toMutableList()  // Convert to mutable list
        Log.d("CartActivity", "Cart Items on Create: $cartItems")

        // Check if the cart is empty and set visibility of views accordingly
        updateCartVisibility()

        // Set up footer icons
        setUpFooterIcons()
    }

    // Function to update cart visibility based on the contents
    private fun updateCartVisibility() {
        Log.d("CartActivity", "Checking Cart Visibility")
        if (cartItems.isEmpty()) {
            binding.emptyCartCard.visibility = View.VISIBLE
            binding.cartRecyclerView.visibility = View.GONE
            binding.tv2.text = "₹ 0.00"  // Ensure total price is zero when the cart is empty
            Log.d("CartActivity", "Cart is Empty. Showing empty cart screen.")
        } else {
            binding.emptyCartCard.visibility = View.GONE
            binding.cartRecyclerView.visibility = View.VISIBLE

            // Initialize the adapter and set it to the RecyclerView
            cartAdapter = CartAdapter(cartItems, this, ::onQuantityChanged, ::onItemRemoved)
            binding.cartRecyclerView.adapter = cartAdapter

            // Calculate and display the total price
            calculateTotalPrice()
        }
    }

    // Function to calculate the total price of all items in the cart
    private fun calculateTotalPrice() {
        val totalPrice = cartItems.sumOf { it.price * it.quantity }
        // Update the TextView with the formatted total price
        Log.d("CartActivity", "Calculated Total Price: ₹ $totalPrice")
        binding.tv2.text = "₹ %.2f".format(totalPrice)
    }

    // Callback for when quantity changes in the adapter
    private fun onQuantityChanged() {
        Log.d("CartActivity", "Quantity changed. Recalculating total price.")
        calculateTotalPrice()
        // Save the updated cart in SharedPreferences
        CartManager.saveCartItems(this, cartItems)
    }

    // Callback for when an item is removed from the cart
    private fun onItemRemoved(position: Int) {
        try {
            Log.d("CartActivity", "Item Removed at Position: $position")

            // Remove the item from the cart list
            cartItems.removeAt(position)

            // Save the updated cart items to SharedPreferences
            CartManager.saveCartItems(this, cartItems)

            // Notify the adapter about the removal
            cartAdapter.notifyItemRemoved(position)

            // Recalculate the total price after item removal
            calculateTotalPrice()

            // Update UI if cart is empty
            updateCartVisibility()  // Make sure visibility is updated when cart is empty
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("CartActivity", "Error removing item from cart: ${e.message}")
        }
    }

    // Set up footer icons
    private fun setUpFooterIcons() {
        // Home icon navigation (slide-in animation)
        binding.iconHome.setOnClickListener {
            val intent = Intent(this, ProductsActivity::class.java)
            val options = android.app.ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left,   // New activity slides in from the right
                R.anim.slide_out_right    // Current activity slides out to the left
            )
            startActivity(intent, options.toBundle())
        }

        // Navigate to About Us screen
        binding.iconAboutUs.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Navigate to Contact Us screen
        binding.iconContact.setOnClickListener {
            val intent = Intent(this, ContactUsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Handle Cart icon (optional functionality)
        binding.iconCart.setOnClickListener {
            // Optionally: Handle the cart icon click
        }
    }

    // Function to add an item to the cart
    fun addToCart(item: CartItem) {
        val currentItems = cartItems.toMutableList()

        // Check if the item already exists in the cart
        val existingItemIndex = currentItems.indexOfFirst { it.id == item.id }
        if (existingItemIndex != -1) {
            // If item exists, update its quantity
            val existingItem = currentItems[existingItemIndex]
            existingItem.quantity += item.quantity
        } else {
            // If item does not exist, add it to the cart
            currentItems.add(item)
        }

        // Save updated cart items to SharedPreferences
        CartManager.saveCartItems(this, currentItems)

        // Update the cart items list and notify the adapter
        cartItems = currentItems
        cartAdapter.notifyDataSetChanged()

        // Recalculate total price
        calculateTotalPrice()

        // Update the cart visibility if needed
        updateCartVisibility()
    }
}
