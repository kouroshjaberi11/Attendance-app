package com.example.attendanceapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var textbox: View
    private lateinit var office: CheckBox
    private lateinit var onsite: CheckBox
    private lateinit var other: CheckBox
    private lateinit var viewLocButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reference = FirebaseDatabase.getInstance().getReference("users")

        val logout = findViewById<Button>(R.id.logout_button)
        office = findViewById(R.id.office_checkbox)
        onsite = findViewById(R.id.onsite_checkbox)
        other = findViewById(R.id.other_checkbox)
        textbox = findViewById<MultiAutoCompleteTextView>(R.id.other_textbox)
        viewLocButton = findViewById(R.id.view_locs)
        textbox.isEnabled = false
        auth = FirebaseAuth.getInstance()

        val submit = findViewById<Button>(R.id.submit_button)

        val db = DatabaseHelper()

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val admin = snapshot.getValue()
                if (admin as Boolean) viewLocButton.visibility = View.VISIBLE

            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        reference.child(auth.currentUser?.uid.toString()).child("admin").addListenerForSingleValueEvent(postListener)

        logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        submit.setOnClickListener {
            if (office.isChecked) {
                db.setLocation(auth.currentUser?.uid.toString(), "Office")
                goToAfterSubmitActivity()
            }else if (onsite.isChecked) {
                db.setLocation(auth.currentUser?.uid.toString(), "Onsite")
                goToAfterSubmitActivity()
            }else if (other.isChecked) {
                //enable text box and get user input
                var loc = (textbox as MultiAutoCompleteTextView).text.toString()
                loc = "Other - " + loc
                db.setLocation(auth.currentUser?.uid.toString(), loc)
                goToAfterSubmitActivity()
            } else {
                Toast.makeText(this, "Please set your location", Toast.LENGTH_SHORT).show()
            }

        }

        viewLocButton.setOnClickListener {
            val intent = Intent(this, LocationsListActivity::class.java)
            startActivity(intent)
        }
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            if (view.id==other.id) {
                textbox.isEnabled = checked
                office.isChecked = false
                onsite.isChecked = false
            } else if (view.id==office.id) {
                textbox.isEnabled = false
                other.isChecked = false
                onsite.isChecked = false
            } else if (view.id==onsite.id) {
                textbox.isEnabled =false
                office.isChecked = false
                other.isChecked = false
            }
        }
    }

    fun goToAfterSubmitActivity() {
        val intent = Intent(this, AfterSubmitActivity::class.java)

        startActivity(intent)
    }
}