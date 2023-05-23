package com.example.attendanceapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class LoginActivity : AppCompatActivity(){

    private val reference = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        val sp = getSharedPreferences("login", MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()
        sp.edit().putString("AP", "iamadmin123").apply()

        if (auth.currentUser!=null) {
            goToMainActivity()
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val login = findViewById<Button>(R.id.login)
        val register = findViewById<Button>(R.id.log_reg)
        val email = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)



        login.setOnClickListener {
            checkUsernamePassword(email, password)
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }
    }

    private fun checkUsernamePassword(email: EditText, password: EditText) {
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this@LoginActivity, OnCompleteListener { task ->
            if (task.isSuccessful) {
                goToMainActivity()
            }
        }).addOnFailureListener(object: OnFailureListener {
            override fun onFailure(e: Exception) {
                email.setError(e.localizedMessage)
                email.requestFocus()
            }

        })
    }

    private fun goToMainActivity() {
        var intent = Intent(this, MainActivity::class.java)

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val location = snapshot.getValue()
                    Log.i("TAG", location.toString())
                    if (!(location?.equals("NONE"))!!) {
                        intent = Intent(this@LoginActivity, AfterSubmitActivity::class.java)
                    }
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        reference.child(auth.currentUser?.uid.toString()).child("location").addListenerForSingleValueEvent(postListener)
    }

}

/*private fun checkUsernamePassword(username: EditText, password: EditText, sp: SharedPreferences) {
        val checkUser = reference.orderByChild("username").equalTo(username.text.toString().toLowerCase())
        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    username.setError(null)
                    password.setError(null)
                    val passFromDb = snapshot.child(username.text.toString().toLowerCase()).child("password").getValue()
                    if (passFromDb.toString().equals(password.text.toString())) {
                        sp.edit().putBoolean("logged_in", true).apply()
                        sp.edit().putString("username", username.text.toString().toLowerCase()).apply()
                        goToMainActivity(sp.getString("username", "").toString())
                    }else {
                        password.setError("Invalid password")
                        password.requestFocus()
                    }
                } else {
                    username.setError("user does not exist")
                    username.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }*/