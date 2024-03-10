package com.example.naukari_group_05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    private val RC_SIGN_IN = 123
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: FirebaseDatabase
    private lateinit var dataReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        dataReference = database.getReference("User")

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set OnClickListener for Google SignIn Button

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val email = auth.currentUser?.email
                    val uid = auth.currentUser?.uid
                    val username = auth.currentUser?.displayName


                    var hashMap: HashMap<String, Any> = HashMap<String, Any>()
                    //adding elements to the hashMap using
                    // put() function
                    hashMap.put("uid", "" + uid)
                    hashMap.put("email", "" + email)
                    hashMap.put("username", "" + username)

                    saveDataToFirebase(uid, hashMap)


                    Toast.makeText(
                        baseContext, "sign In With Google success.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Navigate to your main activity or perform other actions
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun saveDataToFirebase(uid: String?, dataMap: HashMap<String, Any>) {
        // Set a value in the "data" node with the HashMap
        if (uid != null) {
            val query = dataReference.orderByKey().equalTo(uid)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Data exists
                        GoTonext()
                    } else {
                        dataReference.child(uid).setValue(dataMap).addOnSuccessListener {
                            GoTonext();
                        }.addOnFailureListener { exception ->
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    println("Error: ${databaseError.message}")
                }
            })


        }
    }

    private fun GoTonext() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    companion object {
        private const val TAG = "GoogleSignInActivity"
    }


    fun signinwithgoogle(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}

