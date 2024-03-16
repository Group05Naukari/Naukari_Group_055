package com.example.naukari_group_05

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naukari_group_05.Adapter.MyAdapter
import com.example.naukari_group_05.Model.Cadidate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class CandidateActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dataReference: DatabaseReference
    private lateinit var ic_back: ImageView
    val usersList = mutableListOf<Cadidate>()
    private lateinit var recyclerView: RecyclerView


    private lateinit var editserch: EditText


    val myfollower = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.candidate_list)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        dataReference = database.getReference()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        editserch = findViewById<EditText>(R.id.editserch)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        ic_back = findViewById<ImageView>(R.id.ic_back)
        ic_back.setOnClickListener(View.OnClickListener {
            onBackPressed();
        })


        editserch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty()) {
                    filterData(s.toString().lowercase(Locale.getDefault()))
                } else {
                    if (!usersList.isEmpty()) {
                        val adapter = MyAdapter(usersList, this@CandidateActivity)
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        getConnetdUserList();

        fetchDataIntoArray()
    }


    fun filterData(query: CharSequence) {
        val filteredResults = ArrayList<Cadidate>()
        for (model in usersList) {
            if (model.name!!.toLowerCase(Locale.ROOT).contains(query) ||
                model.jobtitle!!.toLowerCase(Locale.ROOT).contains(query)
            ) {
                filteredResults.add(model)
            }
        }

        val adapter = MyAdapter(filteredResults, this@CandidateActivity)
        recyclerView.adapter = adapter
    }


    override fun onResume() {
        super.onResume()
        getConnetdUserList();
        fetchDataIntoArray()
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
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })

    }

    fun fetchDataIntoArray() {
        dataReference.child("candidate")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    usersList.clear();
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(Cadidate::class.java)
                        if (myfollower.contains(user!!.id)) {
                            user.conneted = true;
                        } else {
                            user.conneted = false;
                        }

                        if (user != null) {
                            usersList.add(user)
                        }
                    }
                    val adapter = MyAdapter(usersList, this@CandidateActivity)
                    recyclerView.adapter = adapter
                    // Now, usersList contains all the users from Firebase
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(this@CandidateActivity, MainActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION
            )
        )
    }
}