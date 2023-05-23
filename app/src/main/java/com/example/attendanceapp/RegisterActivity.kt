package com.example.attendanceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        val register = findViewById<Button>(R.id.reg_reg)
        val username = findViewById<EditText>(R.id.Username)
        val email = findViewById<EditText>(R.id.Email)
        val crepass = findViewById<EditText>(R.id.crePass)
        val conpass = findViewById<EditText>(R.id.conPass)
        val can_reg = findViewById<Button>(R.id.cancel_reg)
        val adminBox = findViewById<CheckBox>(R.id.admin_checkbox)
        val adminPass = findViewById<EditText>(R.id.admin_pass)
        val sp = getSharedPreferences("login", MODE_PRIVATE)
        val AP = sp.getString("AP", "")

        reference = FirebaseDatabase.getInstance().getReference("users")

        can_reg.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            if(crepass.text.toString().length < 6 || username.text.toString().length < 6){
                //use for passwords that are too short/empty
                Toast.makeText(this, "username/password is too short", Toast.LENGTH_LONG).show()
            }else if(!(crepass.text.toString().equals(conpass.text.toString()))) {
                //confirm password does not match original password
                conpass.setError("Passwords are not matching")
                conpass.requestFocus()
            } else if (adminBox.isChecked && !AP.equals(adminPass.text.toString())) {
                //incorrect admin password
                adminPass.setError("Invalid admin password")
                adminPass.requestFocus()
            }else {
                //username is new, so add it to the database
                adminPass.setError(null)
                conpass.setError(null)

                checkRegistration(username.text.toString(), email.text.toString(), crepass.text.toString(), adminBox.isChecked)
            }
        }
    }

    private fun checkRegistration(username: String, email: String, password: String, admin: Boolean) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@RegisterActivity, OnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser?.uid
                val db = DatabaseHelper()
                db.newUser(user.toString(), username, admin)
                startActivity(intent)
            }
        }).addOnFailureListener(object: OnFailureListener {
            override fun onFailure(e: Exception) {
                Toast.makeText(this@RegisterActivity, e.localizedMessage, Toast.LENGTH_LONG).show()
            }

        })
    }
}