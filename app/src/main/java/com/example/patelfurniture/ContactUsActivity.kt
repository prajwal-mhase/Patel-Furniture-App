package com.example.patelfurniture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.ImageView

class ContactUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        // Find phone and email TextViews
        val phoneTextView: TextView = findViewById(R.id.phone)
        val emailTextView: TextView = findViewById(R.id.email)

        // Set phone number onClickListener
        phoneTextView.setOnClickListener {
            dialPhoneNumber("+91 9822974444")
        }

        // Set email onClickListener
        emailTextView.setOnClickListener {
            sendEmail("patelanup4444@gmail.com")
        }

        // Set back button onClickListener
        val backButton: ImageView = findViewById(R.id.imageBackButton3)
        backButton.setOnClickListener {
            onBackPressed() // Close the current activity and go back to the previous one
        }
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
        val location = Uri.parse("geo:0,0?q=Yeola+Road,+Kopargaon,+Tal.+Kopargaon,+Dist.+Ahmednagar+423601")
        val intent = Intent(Intent.ACTION_VIEW, location)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
}
