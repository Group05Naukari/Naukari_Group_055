package com.example.naukari_group_05

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class IntroActivity : AppCompatActivity() {

    // Splash screen timeout
    private val SPLASH_TIMEOUT: Long = 3000 // 3 seconds


    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dataReference: DatabaseReference

    var uid = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        dataReference = database.getReference("User")


        var ausss = auth.currentUser


        if(ausss != null){
            uid = auth.currentUser!!.uid


            CheckData(uid);
        }else{
            Handler().postDelayed({
                // Start your main activity after the splash timeout
//                startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
//                // Close this activity
//                finish()
            }, SPLASH_TIMEOUT)
            Toast.makeText(
                this@IntroActivity,
                "Data Not already exists.:",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Delayed execution of the next activity using a Handler

    }

    private fun CheckData(uid: String?) {
        // Set a value in the "data" node with the HashMap
        if (uid != null) {
            val query = dataReference.orderByKey().equalTo(uid)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Data exists
                        Handler().postDelayed({
                            // Start your main activity after the splash timeout
                            startActivity(Intent(this@IntroActivity, MainActivity::class.java))

                            // Close this activity
                            finish()
                        }, SPLASH_TIMEOUT)
                        Toast.makeText(
                            this@IntroActivity,
                            "Data already exists.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Handler().postDelayed({
                            // Start your main activity after the splash timeout
//                            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
//                            // Close this activity
//                            finish()
                        }, SPLASH_TIMEOUT)
                        Toast.makeText(
                            this@IntroActivity,
                            "Data Not already exists.:",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    println("Error: ${databaseError.message}")
                }
            })
        } else {
            Handler().postDelayed({
                // Start your main activity after the splash timeout
//                startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
//                // Close this activity
//                finish()
            }, SPLASH_TIMEOUT)
            Toast.makeText(
                this@IntroActivity,
                "Data Not already exists.:",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}