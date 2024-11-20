package com.example.patelfurniture

import android.os.Bundle
import android.widget.ImageView  // Import ImageView class
import androidx.appcompat.app.AppCompatActivity

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us) // Your layout file

        // Find the back button ImageView by its ID
        val backButton = findViewById<ImageView>(R.id.imageBackButton2)

        // Set an OnClickListener to handle the back button functionality
        backButton.setOnClickListener {
            // When clicked, finish the current activity and go back to the previous activity
            onBackPressed()  // This will trigger the system's back functionality
        }
    }
}
