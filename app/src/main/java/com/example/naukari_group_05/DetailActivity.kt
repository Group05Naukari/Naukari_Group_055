package com.example.naukari_group_05

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.naukari_group_05.Model.Cadidate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {


    private lateinit var profile_picture: ImageView
    private lateinit var user_name: TextView
    private lateinit var headline: TextView
    private lateinit var aboutus: TextView
    private lateinit var ic_back: ImageView

    private lateinit var college_degree: TextView

    private lateinit var connect: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dataReference: DatabaseReference
    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.candidate_details)


        val intent = intent
        val receivedData = intent.getParcelableExtra<Cadidate>("Cadidatedata")

        ic_back = findViewById<ImageView>(R.id.ic_back)
        ic_back.setOnClickListener(View.OnClickListener {
            onBackPressed();
        })


        // Set OnClickListener for Google SignIn Button
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        uid = auth.currentUser!!.uid

        profile_picture = findViewById<ImageView>(R.id.profile_picture)
        user_name = findViewById<TextView>(R.id.user_name)
        headline = findViewById<TextView>(R.id.headline)
        aboutus = findViewById<TextView>(R.id.aboutus)
        college_degree = findViewById<TextView>(R.id.Education)

        connect = findViewById<TextView>(R.id.connect)


        if (receivedData != null) {
            Glide.with(this)
                .load(receivedData.photo_url)
                .into(profile_picture)

        }




        user_name.text = receivedData!!.name
        headline.text = receivedData!!.jobtitle
        aboutus.text = receivedData!!.about_us
        aboutus.text = receivedData!!.about_us
        college_degree.text = receivedData!!.college_degree

        if (receivedData.conneted == true) {
            connect.text = "connected"
        } else {
            connect.text = "connect"
        }

        connect.setOnClickListener(View.OnClickListener {


            if (receivedData.conneted == true) {
                Toast.makeText(
                    this@DetailActivity,
                    "You are Allerady Conneced With " + receivedData.name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                dataReference = database.getReference("User").child(uid)

                dataReference.child("connection").push().setValue(receivedData.id)
                    .addOnSuccessListener {
                        connect.text = "connected"
                        // Successfully pushed new child
                        Toast.makeText(

                            this@DetailActivity,
                            "You are Conneced With " + receivedData.name,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        // Failed to push new child

                    }

            }


        })

    }

}