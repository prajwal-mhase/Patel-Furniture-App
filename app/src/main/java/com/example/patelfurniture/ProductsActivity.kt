package com.example.patelfurniture

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class ProductsActivity : AppCompatActivity() {

    private var areFirstBoxItemsAdded = false // Flag to track if new boxes are added for the first box
    private var areSecondBoxItemsAdded = false // Flag to track if new boxes are added for the second box

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        // Find the back button and set an OnClickListener to navigate back
        val backButton = findViewById<ImageView>(R.id.imageBackButton)
        backButton.setOnClickListener {
            onBackPressed() // This will handle the back navigation
        }

        // Find the container (LinearLayout) that holds the boxes
        val boxesContainer = findViewById<LinearLayout>(R.id.boxesContainer)

        // Find the first box (MaterialCardView)
        val firstBox = findViewById<MaterialCardView>(R.id.firstBox)

        // Set an OnClickListener to add/remove boxes when the first box is clicked
        firstBox.setOnClickListener {
            if (areFirstBoxItemsAdded) {
                removeAdditionalBoxes(boxesContainer, "first")
            } else {
                addAdditionalBoxes(boxesContainer, "first")
            }
        }

        // Find the second box (MaterialCardView)
        val secondBox = findViewById<MaterialCardView>(R.id.secondBox)

        // Set an OnClickListener to add/remove boxes when the second box is clicked
        secondBox.setOnClickListener {
            if (areSecondBoxItemsAdded) {
                removeAdditionalBoxes(boxesContainer, "second")
            } else {
                addAdditionalBoxes(boxesContainer, "second")
            }
        }
    }

    // Function to dynamically add more boxes with specific labels and images
    private fun addAdditionalBoxes(container: LinearLayout, clickedBox: String) {
        // Specific labels for each additional box
        val labels = if (clickedBox == "first") {
            listOf("18 MM", "12 MM", "8 MM", "6 MM") // Normal order for the first box
        } else {
            listOf("6 MM", "8 MM", "12 MM", "18 MM") // Reversed order for the second box
        }

        // Loop through the labels list and add each new box with the correct label
        for (index in labels.indices) {
            val label = labels[index]

            val newBox = MaterialCardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    100 // Height for the box
                ).apply {
                    setMargins(0, 16, 0, 16) // Add margin between boxes
                }
                setCardElevation(8f)
                setRadius(16f)
                setCardBackgroundColor(resources.getColor(android.R.color.white, theme))

                // Add a TextView inside the MaterialCardView
                val textView = TextView(this@ProductsActivity).apply {
                    text = label
                    textSize = 22f
                    setTextColor(resources.getColor(R.color.purple_500, theme)) // Use the defined color
                    gravity = Gravity.CENTER
                }

                addView(textView)
            }

            // Add the new boxes after the first or second box, depending on which one was clicked
            if (clickedBox == "first") {
                container.addView(newBox, container.indexOfChild(container.findViewById(R.id.secondBox)))
                // Add 1x2 grid below the label
                create1x2Grid(container, newBox, "Vista M. R. 303", label)
            } else if (clickedBox == "second") {
                container.addView(newBox, container.indexOfChild(container.findViewById(R.id.secondBox)) + 1)
                // Add 1x2 grid below the label
                create1x2Grid(container, newBox, "Kitply 710", label)
            }
        }

        // Update flags to track whether boxes were added or not
        if (clickedBox == "first") {
            areFirstBoxItemsAdded = true
        } else if (clickedBox == "second") {
            areSecondBoxItemsAdded = true
        }
    }

    private fun create1x2Grid(container: LinearLayout, parentBox: MaterialCardView, boxType: String, materialSize: String) {
        val gridLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(8, 8, 8, 8)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val boxWidth = resources.getDimensionPixelSize(R.dimen.default_box_width)
        val boxHeight = resources.getDimensionPixelSize(R.dimen.default_box_height)

        // Loop to add two grid boxes (1x2 grid)
        for (i in 0 until 2) {
            val gridBox = MaterialCardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(boxWidth, boxHeight).apply {
                    setMargins(8, 8, 8, 8)
                }
                setCardElevation(4f)
                setRadius(16f)
                setCardBackgroundColor(resources.getColor(android.R.color.white, theme))
            }

            // Create and set the ImageView inside the grid box
            val imageView = ImageView(this@ProductsActivity).apply {
                val imageRes = when {
                    boxType == "Vista M. R. 303" && materialSize == "18 MM" && i == 0 -> R.drawable.img_kv_18_8x4
                    boxType == "Vista M. R. 303" && materialSize == "18 MM" && i == 1 -> R.drawable.img_kv_18_7x4
                    boxType == "Vista M. R. 303" && materialSize == "12 MM" && i == 0 -> R.drawable.img_kv_12_8x4
                    boxType == "Vista M. R. 303" && materialSize == "12 MM" && i == 1 -> R.drawable.img_kv_12_7x4
                    boxType == "Vista M. R. 303" && materialSize == "8 MM" && i == 0 -> R.drawable.img_kv_8_8x4
                    boxType == "Vista M. R. 303" && materialSize == "8 MM" && i == 1 -> R.drawable.img_kv_8_7x4
                    boxType == "Vista M. R. 303" && materialSize == "6 MM" && i == 0 -> R.drawable.img_kv_6_8x4
                    boxType == "Vista M. R. 303" && materialSize == "6 MM" && i == 1 -> R.drawable.img_kv_6_7x4
                    boxType == "Kitply 710" && materialSize == "18 MM" && i == 0 -> R.drawable.img_k_18_8x4
                    boxType == "Kitply 710" && materialSize == "18 MM" && i == 1 -> R.drawable.img_k_18_7x4
                    boxType == "Kitply 710" && materialSize == "12 MM" && i == 0 -> R.drawable.img_k_12_8x4
                    boxType == "Kitply 710" && materialSize == "12 MM" && i == 1 -> R.drawable.img_k_12_7x4
                    boxType == "Kitply 710" && materialSize == "8 MM" && i == 0 -> R.drawable.img_k_8_8x4
                    boxType == "Kitply 710" && materialSize == "8 MM" && i == 1 -> R.drawable.img_k_8_7x4
                    boxType == "Kitply 710" && materialSize == "6 MM" && i == 0 -> R.drawable.img_k_6_8x4
                    boxType == "Kitply 710" && materialSize == "6 MM" && i == 1 -> R.drawable.img_k_6_7x4
                    else -> throw IllegalArgumentException("Invalid box type or material size")
                }
                setImageResource(imageRes)

                // Set margin for the ImageView
                val imageParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    setMargins(20, 20, 20, 300) // Add margin to the image (4 sides)
                }
                layoutParams = imageParams
            }

            // Add the image view to the gridBox
            gridBox.addView(imageView)

            // Create the description TextView and add below the image
            val descriptionText = TextView(this@ProductsActivity).apply {
                val companyName: String
                val price: String
                val size: String // Declare a variable for the size

                // Provide the description based on image details
                when {
                    boxType == "Vista M. R. 303" && materialSize == "18 MM" && i == 0 -> {
                        companyName = "Vista M. R. 303"
                        size = "8x4 Feet"
                        price = "Rs 2880.00"
                    }
                    boxType == "Vista M. R. 303" && materialSize == "18 MM" && i == 1 -> {
                        companyName = "Vista M. R. 303"
                        size = "7x4 Feet"
                        price = "Rs 2520.00"
                    }
                    boxType == "Vista M. R. 303" && materialSize == "12 MM" && i == 0 -> {
                        companyName = "Vista M. R. 303"
                        size = "8x4 Feet"
                        price = "Rs 2240.00"
                    }
                    boxType == "Vista M. R. 303" && materialSize == "12 MM" && i == 1 -> {
                        companyName = "Vista M. R. 303"
                        size = "7x4 Feet"
                        price = "Rs 1960.00"
                    }
                    boxType == "Vista M. R. 303" && materialSize == "8 MM" && i == 0 -> {
                        companyName = "Vista M. R. 303"
                        size = "8x4 Feet"
                        price = "Rs 1920.00"
                    }
                    boxType == "Vista M. R. 303" && materialSize == "8 MM" && i == 1 -> {
                        companyName = "Vista M. R. 303"
                        size = "7x4 Feet"
                        price = "Rs 1680.00"
                    }
                    boxType == "Vista M. R. 303" && materialSize == "6 MM" && i == 0 -> {
                        companyName = "Vista M. R. 303"
                        size = "8x4 Feet"
                        price = "Rs 1280.00"
                    }
                    boxType == "Vista M. R. 303" && materialSize == "6 MM" && i == 1 -> {
                        companyName = "Vista M. R. 303"
                        size = "7x4 Feet"
                        price = "Rs 1120.00"
                    }
                    boxType == "Kitply 710" && materialSize == "18 MM" && i == 0 -> {
                        companyName = "Kitply 710"
                        size = "8x4 Feet"
                        price = "Rs 3520.00"
                    }
                    boxType == "Kitply 710" && materialSize == "18 MM" && i == 1 -> {
                        companyName = "Kitply 710"
                        size = "7x4 Feet"
                        price = "Rs 3220.00"
                    }
                    boxType == "Kitply 710" && materialSize == "12 MM" && i == 0 -> {
                        companyName = "Kitply 710"
                        size = "8x4 Feet"
                        price = "Rs 2400.00"
                    }
                    boxType == "Kitply 710" && materialSize == "12 MM" && i == 1 -> {
                        companyName = "Kitply 710"
                        size = "7x4 Feet"
                        price = "Rs 2100.00"
                    }
                    boxType == "Kitply 710" && materialSize == "8 MM" && i == 0 -> {
                        companyName = "Kitply 710"
                        size = "8x4 Feet"
                        price = "Rs 2080.00"
                    }
                    boxType == "Kitply 710" && materialSize == "8 MM" && i == 1 -> {
                        companyName = "Kitply 710"
                        size = "7x4 Feet"
                        price = "Rs 1820.00"
                    }
                    boxType == "Kitply 710" && materialSize == "6 MM" && i == 0 -> {
                        companyName = "Kitply 710"
                        size = "8x4 Feet"
                        price = "Rs 1760.00"
                    }
                    boxType == "Kitply 710" && materialSize == "6 MM" && i == 1 -> {
                        companyName = "Kitply 710"
                        size = "7x4 Feet"
                        price = "Rs 1540.00"
                    }
                    else -> {
                        companyName = "Unknown"
                        size = "Unknown"
                        price = "Rs 00.00"
                    }
                }

                // Create a SpannableString to set bold for company name and normal for size and price
                val description = SpannableString("$companyName\nSize: $size\nPrice: $price")
                description.setSpan(StyleSpan(Typeface.BOLD), 0, companyName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                text = description
                textSize = 16f
                setTextColor(resources.getColor(R.color.black, theme)) // Set text color
                gravity = Gravity.CENTER
                setPadding(0, 500, 0, 8) // Add padding between image and description
            }

            // Add the description below the image
            gridBox.addView(descriptionText)

            // Add the gridBox to the gridLayout
            gridLayout.addView(gridBox)
        }

        // Insert the gridLayout below the current label box
        val index = container.indexOfChild(parentBox) + 1
        container.addView(gridLayout, index)
    }




    // Function to remove the newly added boxes and grids
    private fun removeAdditionalBoxes(container: LinearLayout, clickedBox: String) {
        val childCount = container.childCount
        for (i in childCount - 1 downTo 0) {
            val childView = container.getChildAt(i)

            // Remove dynamically added boxes or grids (MaterialCardView and LinearLayout)
            if (childView is LinearLayout && childView.orientation == LinearLayout.HORIZONTAL) {
                container.removeViewAt(i)
            } else if (childView is MaterialCardView && childView.id != R.id.firstBox && childView.id != R.id.secondBox) {
                container.removeViewAt(i)
            }
        }

        // Reset the flags after removal
        if (clickedBox == "first") {
            areFirstBoxItemsAdded = false
        } else if (clickedBox == "second") {
            areSecondBoxItemsAdded = false
        }
    }
}
