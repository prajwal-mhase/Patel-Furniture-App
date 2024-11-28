package com.example.patelfurniture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.patelfurniture.databinding.ActivityContactUsBinding

class ContactUsActivity : AppCompatActivity() {

    // Declare a binding variable
    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find phone and email TextViews using binding
        val phoneTextView = binding.phone
        val emailTextView = binding.email

        // Set phone number onClickListener
        phoneTextView.setOnClickListener {
            dialPhoneNumber("+91 9822974444")
        }

        // Set email onClickListener
        emailTextView.setOnClickListener {
            sendEmail("patelanup4444@gmail.com")
        }

        // Set up footer icons
        setUpFooterIcons()
    }

    // Function to dial a phone number
    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }

    // Function to send an email
    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry from Patel Furniture")
        startActivity(intent)
    }

    // Function to open Google Maps
    fun openGoogleMaps(view: View) {
        val location = Uri.parse("https://maps.app.goo.gl/3Z9yVoRNAnLPVRQk6")
        val intent = Intent(Intent.ACTION_VIEW, location)
        startActivity(intent)
    }

    // Function to set up footer icons
    private fun setUpFooterIcons() {
        // Icon 1 (Product Activity)
        binding.iconHome.setOnClickListener {
            val intent = Intent(this, ProductsActivity::class.java)
            startActivity(intent)
            // Left-to-right slide-in and right-to-left slide-out transition
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.iconCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            // Left-to-right slide-in and right-to-left slide-out transition
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // Icon 2 (About Us Activity)
        binding.iconAboutUs.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
            // Left-to-right slide-in and right-to-left slide-out transition
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}
