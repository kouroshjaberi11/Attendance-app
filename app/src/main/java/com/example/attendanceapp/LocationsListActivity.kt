package com.example.attendanceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class LocationsListActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<*>
    private lateinit var arrayLocPpl: Array<String>
    private lateinit var lv_listView: ListView
    private lateinit var tv_emptyTextView: TextView
    private val reference = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations_list)

        lv_listView = findViewById(R.id.lv_listView)
        tv_emptyTextView = findViewById(R.id.tv_emptyTextView)

        var i=0

        val checkQuery = reference.orderByChild("username")
        checkQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                val children = snapshot.children
                arrayLocPpl = Array(snapshot.childrenCount.toInt(), { i -> (i*1).toString()})

                children.forEach {
                    var user = it.child("username").getValue().toString()
                    var location = it.child("location").getValue().toString()

                    arrayLocPpl[i] = "$user - $location"
                    i++
                }
                i=0

                adapter = ArrayAdapter(this@LocationsListActivity, android.R.layout.simple_list_item_1, arrayLocPpl)

                lv_listView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })


        lv_listView.emptyView = tv_emptyTextView

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

        val search: MenuItem? = menu?.findItem(R.id.nav_search)
        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search by employee name or location"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}

