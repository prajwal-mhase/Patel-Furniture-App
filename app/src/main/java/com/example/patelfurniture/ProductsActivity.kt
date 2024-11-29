package com.example.patelfurniture

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patelfurniture.databinding.ActivityProductsBinding
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductsBinding
    private val database = FirebaseDatabase.getInstance()
    private val productsRef = database.getReference("products")

    private val cartItems = mutableListOf<CartItem>() // Store the cart items
    private var networkReceiver: NetworkReceiver? = null // To listen for network changes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding the layout with the activity
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Show progress bar before data is loaded
        binding.progressBar.visibility = View.VISIBLE

        // Fetch data from Firebase
        fetchProductsData()

        // Set up the right icon click listener to show the custom popup
        binding.rightIcon.setOnClickListener { v -> showCustomPopup(v) }

        // Set up footer icons click listeners
        setUpFooterIcons()
    }

    override fun onStart() {
        super.onStart()
        // Register the network receiver to listen for connectivity changes
        networkReceiver = NetworkReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        // Unregister the network receiver when the activity stops
        networkReceiver?.let {
            unregisterReceiver(it)
        }
    }

    private fun fetchProductsData() {
        // Check if the internet is available
        if (!isNetworkAvailable(this)) {
            // Show a toast or update a TextView in the layout to notify the user
            Toast.makeText(this, "Unable to connect. Please check your internet connection.", Toast.LENGTH_LONG).show()

            // Optionally, show a message in the UI instead of a Toast
            binding.boxesContainer.removeAllViews()
            val errorMessageView = TextView(this).apply {
                text = "Unable to load products. Please check your internet connection."
                setTextColor(Color.RED)
                textSize = 16f
                gravity = Gravity.CENTER
                setPadding(16, 16, 16, 16)
            }
            binding.boxesContainer.addView(errorMessageView)

            // Hide the progress bar
            binding.progressBar.visibility = View.GONE
            return
        }

        // Show progress bar while fetching data
        binding.progressBar.visibility = View.VISIBLE

        // Fetch products from Firebase
        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear any previous data
                binding.boxesContainer.removeAllViews()

                // If there are no products, show a "No products available" message
                if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    val noProductsMessage = TextView(this@ProductsActivity).apply {
                        text = "No products available."
                        setTextColor(Color.GRAY)
                        textSize = 16f
                        gravity = Gravity.CENTER
                        setPadding(16, 16, 16, 16)
                    }
                    binding.boxesContainer.addView(noProductsMessage)
                } else {
                    // Loop through the products and display them
                    for (productSnapshot in dataSnapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        if (product != null) {
                            // Inflate the product layout
                            val productView = LayoutInflater.from(this@ProductsActivity).inflate(R.layout.item_product, binding.boxesContainer, false)

                            // Set the product name, size, price, and category
                            productView.findViewById<TextView>(R.id.productNameTextView).text = product.name
                            productView.findViewById<TextView>(R.id.productSizeTextView).text = product.size
                            productView.findViewById<TextView>(R.id.productPriceTextView).text = "₹${product.price}"
                            productView.findViewById<TextView>(R.id.productCategoryTextView).text = product.category // New category field

                            // Add the product view to the container
                            binding.boxesContainer.addView(productView)

                            // Add to cart functionality
                            val addToCartButton = productView.findViewById<View>(R.id.addToCartButton)
                            addToCartButton.setOnClickListener {
                                addToCart(product)
                            }
                        }
                    }
                }

                // Hide the progress bar when data is loaded
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error (could happen due to database issues or no internet)
                Toast.makeText(applicationContext, "Failed to load data. Please try again later.", Toast.LENGTH_SHORT).show()

                // Hide the progress bar in case of error
                binding.progressBar.visibility = View.GONE
            }
        })
    }


    private fun addToCart(product: Product) {
        // Generate a unique ID for the cart item (using System.currentTimeMillis())
        val newItem = CartItem(
            id = System.currentTimeMillis().toInt(), // Generate a unique ID
            name = product.name,
            category = product.category,
            price = product.price,
            size = product.size,
            quantity = 1
        )

        // Add to global cart using CartManager
        CartManager.addToCart(this, newItem)

        // Show a toast message to the user
        Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
    }

    private var popupWindow: PopupWindow? = null // Make it a class variable

    private fun showCustomPopup(view: View) {
        // If the popup is already showing, dismiss it when the menu icon is clicked again
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow?.dismiss()
            return
        }

        // Inflate the custom layout for the PopupWindow
        val inflater = layoutInflater
        val popupView = inflater.inflate(R.layout.custom_popup_menu, null)

        // Create the PopupWindow with the custom layout
        popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        // Make the PopupWindow focusable so that it closes when the user taps outside
        popupWindow?.isFocusable = true
        popupWindow?.isOutsideTouchable = true

        // Set the PopupWindow to show below the right icon with left margin
        val location = IntArray(2)
        view.getLocationOnScreen(location)

        val leftMargin = -150 // Adjust this value for the desired left margin in pixels

        // Show the popup window below the right icon, with an added left margin
        popupWindow?.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + leftMargin, location[1] + view.height)

        // Set click listener on the "Logout" option inside the MaterialCardView
        popupView.findViewById<MaterialCardView>(R.id.logoutCard).setOnClickListener {
            // Log the user out from Firebase
            FirebaseAuth.getInstance().signOut()

            // Show a toast message
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Redirect to the LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the activity stack
            startActivity(intent)

            // Close the popup
            popupWindow?.dismiss()
        }

        // Close the popup when clicked outside
        popupWindow?.setOnDismissListener {
            popupWindow = null // Nullify the reference when popup is dismissed
        }
    }

    // Set up footer icons to navigate to different activities
    private fun setUpFooterIcons() {
        binding.iconCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java).apply {
                putParcelableArrayListExtra("cartItems", ArrayList(cartItems))
            }
            startActivity(intent)
            // Apply right-to-left slide transition
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.iconAboutUs.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
            // Apply right-to-left slide transition
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Icon 3 (Contact Us Activity)
        binding.iconContact.setOnClickListener {
            val intent = Intent(this, ContactUsActivity::class.java)
            startActivity(intent)
            // Apply right-to-left slide transition
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    // Updated Product class with no-argument constructor for Firebase
    data class Product(
        val id: Int = 0,
        val name: String = "",
        val category: String = "",
        val size: String = "",
        var quantity: Int = 1,
        val price: Double = 0.0
    ) {
        constructor() : this(0, "", "", "", 1, 0.0)
    }

    // Function to check internet connectivity
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    // BroadcastReceiver to listen for network changes
    inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isNetworkAvailable(this@ProductsActivity)) {
                // When network is available, reload the data
                fetchProductsData()
            }
        }
    }
}
