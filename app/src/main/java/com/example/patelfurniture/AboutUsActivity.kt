package com.example.patelfurniture

import android.os.Bundle
import android.content.Intent
import android.widget.ImageView
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.patelfurniture.R
import com.example.patelfurniture.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)




        setUpFooterIcons()
    }

    private fun setUpFooterIcons() {


        // For iconHome (left to right animation)
        binding.iconHome.setOnClickListener {
            val intent = Intent(this, ProductsActivity::class.java)
            val options = android.app.ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left,   // Enter animation (new activity slides in from the left)
                R.anim.slide_out_right  // Exit animation (current activity slides out to the right)
            )
            startActivity(intent, options.toBundle())
        }

        binding.iconCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            val options = android.app.ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left,   // Enter animation (new activity slides in from the left)
                R.anim.slide_out_right  // Exit animation (current activity slides out to the right)
            )
            startActivity(intent, options.toBundle())
        }



        // For iconContact (right to left animation)
        binding.iconContact.setOnClickListener {
            val intent = Intent(this, ContactUsActivity::class.java)
            val options = android.app.ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,  // Enter animation (new activity slides in from the right)
                R.anim.slide_out_left   // Exit animation (current activity slides out to the left)
            )
            startActivity(intent, options.toBundle())
        }
    }


}
