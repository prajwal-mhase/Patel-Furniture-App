package com.example.patelfurniture

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // UI components
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val loginTextView = findViewById<TextView>(R.id.loginTextView)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Handle Sign Up Button Click
        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Validation checks
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show the progress bar when the sign-up process starts
            progressBar.visibility = ProgressBar.VISIBLE

            // Proceed with Firebase authentication
            signUpWithEmail(email, password)
        }

        // Handle Login Text Click (redirect to LoginActivity)
        loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Validate email format
    private fun isValidEmail(input: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    // Sign up with email and password
    private fun signUpWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // Hide the progress bar after the operation is complete
                progressBar.visibility = ProgressBar.GONE

                if (task.isSuccessful) {
                    // Sign-up was successful
                    val currentUser = auth.currentUser
                    currentUser?.let {
                        val userId = it.uid
                        val email = it.email

                        // Get a reference to the database and store the user's email
                        val database = FirebaseDatabase.getInstance().reference
                        val userMap = hashMapOf<String, String>(
                            "email" to email.orEmpty()
                        )

                        // Store the email under the user's unique ID
                        database.child("users").child(userId).setValue(userMap)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    // After successful sign-up and data saving, sign out the user
                                    auth.signOut()

                                    // Notify user and redirect to LoginActivity
                                    Toast.makeText(this, "Sign-up successful! Please log in.", Toast.LENGTH_SHORT).show()

                                    // Redirect to LoginActivity
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish() // Close SignUpActivity so the user can't navigate back
                                } else {
                                    Toast.makeText(this, "Error saving user data: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    // Handle sign-up failure
                    Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
