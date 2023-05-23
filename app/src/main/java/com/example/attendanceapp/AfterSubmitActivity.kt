package com.example.attendanceapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AfterSubmitActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_submit)
        val displayLoc = findViewById<TextView>(R.id.disp_loc)
        val changeLocButton = findViewById<Button>(R.id.change_loc_button)
        val logoutButton = findViewById<Button>(R.id.logout_button2)
        val listEmpLocs = findViewById<Button>(R.id.view_emp_locs)
        val reference = FirebaseDatabase.getInstance().getReference("users")

        auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser?.uid.toString()

        val adminPostListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val admin = snapshot.getValue()
                if (admin as Boolean) listEmpLocs.visibility = View.VISIBLE
                else listEmpLocs.visibility = View.INVISIBLE
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }

        reference.child(uid).child("admin").addListenerForSingleValueEvent(adminPostListener)


        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val location = snapshot.getValue()
                displayLoc.text = "Your current location is: $location"
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        reference.child(uid).child("location").addListenerForSingleValueEvent(postListener)

        changeLocButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        listEmpLocs.setOnClickListener {
            val intent = Intent(this, LocationsListActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}