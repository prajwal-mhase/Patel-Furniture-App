package com.example.patelfurniture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class WelcomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        // Find the "Get Started" button
        val getStartedButton = findViewById<Button>(R.id.getStartedButton)

        // Set an OnClickListener to navigate to MainActivity when clicked
        getStartedButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish the WelcomePageActivity so user cannot return to it
        }
    }
}
