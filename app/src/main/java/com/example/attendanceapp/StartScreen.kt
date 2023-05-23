package com.example.attendanceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StartScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)
        val reference = FirebaseDatabase.getInstance().getReference("users")
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser==null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else{
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val location = snapshot.getValue()
                        if (location?.equals("NONE")!!) {
                            goToMainActivity()
                        } else {
                            val intent = Intent(this@StartScreen, AfterSubmitActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
            reference.child(auth.currentUser?.uid.toString()).child("location").addListenerForSingleValueEvent(postListener)
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}