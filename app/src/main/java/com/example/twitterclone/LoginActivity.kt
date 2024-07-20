package com.example.twitterclone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.twitterclone.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var mauth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        if(mauth.currentUser != null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {

            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            login(email , password)

        }

        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            signup(email , password)
        }

    }

    private fun login(email: String, password: String){


            mauth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success,

                        val intent = Intent(this , HomeActivity::class.java)
                        startActivity(intent)


                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()

                    }
                }
        }

    private fun signup(email: String, password: String){

        mauth.createUserWithEmailAndPassword(email , password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    // add user to firebase database

                    val listOfTweets = mutableListOf<String>()
                    listOfTweets.add("")

                    val listOfFollowers = mutableListOf<String>()
                    listOfFollowers.add("")


                    val user = User(
                        email = email,
                        userProfileImage = "",
                        listOfFollowers = listOfFollowers,
                        listOfTweets = listOfTweets,
                      // uid = mauth.currentUser!!.uid
                        uid = mauth.uid.toString()
                    )

                    addUserToDatabase(user)

                    // move to home activity
                    val intent = Intent(this , HomeActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun addUserToDatabase(user : User){
        Firebase.database.getReference("users").child(user.uid).setValue(user)
    }





    private fun init(){

        edtPassword = findViewById(R.id.edtPassword)
        edtEmail = findViewById(R.id.edtEmail)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        mauth = Firebase.auth

    }
}