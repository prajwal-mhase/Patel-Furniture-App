package com.example.patelfurniture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the MaterialCardView for the first square (Products)
        val productsSquare = findViewById<MaterialCardView>(R.id.productsSquare)

        // Set an OnClickListener to navigate to ProductsActivity
        productsSquare.setOnClickListener {
            val intent = Intent(this, ProductsActivity::class.java)
            startActivity(intent)
        }

        // Find the MaterialCardView for the second square (About Us)
        val aboutUsSquare = findViewById<MaterialCardView>(R.id.aboutUsSquare)

        // Set an OnClickListener to navigate to AboutUsActivity
        aboutUsSquare.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
        }

        // Find the MaterialCardView for the third square (Contact Us)
        val contactUsSquare = findViewById<MaterialCardView>(R.id.contactUsSquare)

        // Set an OnClickListener to navigate to ContactUsActivity
        contactUsSquare.setOnClickListener {
            val intent = Intent(this, ContactUsActivity::class.java)
            startActivity(intent)
        }
    }
}
