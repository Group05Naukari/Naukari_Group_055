package com.example.naukari_group_05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naukari_group_05.Adapter.FeedAdapter
import com.example.naukari_group_05.Model.FeedItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    private lateinit var managenetwork: LinearLayout
    private lateinit var database: FirebaseDatabase
    private lateinit var dataReference: DatabaseReference
    val feedList = ArrayList<FeedItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var addtoconnect: LinearLayout


    val myfollower = mutableListOf<String>()


    override fun onResume() {
        super.onResume()
        getConnetdUserList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance()
        dataReference = database.getReference()


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        dataReference = database.getReference()


        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        addtoconnect = findViewById<LinearLayout>(R.id.addtoconnect)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager



        managenetwork = findViewById<LinearLayout>(R.id.managenetwork)
        managenetwork.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(
                    this@MainActivity,
                    CandidateActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )

        })
        addtoconnect.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(
                    this@MainActivity,
                    CandidateActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )

        })


        getConnetdUserList()
    }


    private fun getConnetdUserList() {
        dataReference.child("User").child(auth.currentUser!!.uid).child("connection")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (userSnapshot in dataSnapshot.children) {
                        val value = userSnapshot.value
                        myfollower.add(value.toString())
                        Log.w("maydatatttaa", "" + value.toString())
                    }
                    if (myfollower.isEmpty()) {
                        addtoconnect.visibility = View.VISIBLE
                    } else {
                        fetchDataIntoArray()
                        addtoconnect.visibility = View.GONE
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })

    }


    fun fetchDataIntoArray() {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("candidate")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                feedList.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val feedSnapshot2 = userSnapshot.child("id").getValue()
                    val feedSnapshot = userSnapshot.child("feed")
                    for (feedSnapshotChild in feedSnapshot.children) {
                        val author =
                            feedSnapshotChild.child("author").getValue(String::class.java) ?: ""
                        val content =
                            feedSnapshotChild.child("content").getValue(String::class.java) ?: ""
                        val headline =
                            feedSnapshotChild.child("headline").getValue(String::class.java) ?: ""
                        val id = feedSnapshotChild.child("id").getValue(String::class.java) ?: ""
                        val timestamp =
                            feedSnapshotChild.child("timestamp").getValue(String::class.java) ?: ""

                        val feedItem = FeedItem(author, content, headline, id, timestamp)


                        if (myfollower.contains(feedSnapshot2)) {
                            feedList.add(feedItem)
                        } else {

                        }



                        feedList.sortByDescending { it.timestamp }

                    }
                }

                val adapter = FeedAdapter(feedList, this@MainActivity)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}