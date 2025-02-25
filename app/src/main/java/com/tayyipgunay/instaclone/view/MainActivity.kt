package com.tayyipgunay.instaclone.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.tayyipgunay.instaclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private  var firebaseUser:FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
// Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

       // val currentUser=auth.currentUser//fireBase user'a dönüşüyor.
        firebaseUser=auth.currentUser
        updateUI(firebaseUser)



        /*firebaseUser=auth.currentUser
        updateUI(firebaseUser)*/


        /*if(currentUser!=null){
             val intent=Intent(this@MainActivity, FeedActivitiy::class.java)
             startActivity(intent)
             finish()
         }*/
        /* enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets*/
    }
    private fun updateUI(user:FirebaseUser ?) {
        if (user != null) {
            // Kullanıcı oturum açtıysa FeedActivity'e yönlendir
            val intent = Intent(this@MainActivity, FeedActivitiy::class.java)
            startActivity(intent)
            finish() // MainActivity'yi kapatıyoruz
        } else {
            // Kullanıcı oturum açmamışsa giriş sayfasında kalır
            Toast.makeText(this@MainActivity, "Lütfen giriş yapın", Toast.LENGTH_LONG).show()
        }
    }

    fun signUpClicked(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if (!email.isNotEmpty() || !password.isNotEmpty()) {
            Toast.makeText(this@MainActivity, " lütfen boş yerleri doldurun", Toast.LENGTH_LONG)
                .show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                val intent = Intent(this@MainActivity, FeedActivitiy::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }
    }

    fun signInClicked(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@MainActivity, "Lütfen boş yerleri doldurun", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                       firebaseUser=auth.currentUser
                        /*
                         Bu satır, giriş başarılı olduğunda Firebase'den oturum açan kullanıcının bilgilerini
                         günceller ve firebaseUser değişkenine atar.
                         Eğer bu satırı yazmazsan, firebaseUser güncellenmez ve önceki değeri kalır.
                        */

                        updateUI(firebaseUser)  // Başarılı giriş sonrası UI'yi güncelle
                    } else {
                        Toast.makeText(this@MainActivity, "Giriş Başarısız", Toast.LENGTH_LONG).show()
                        updateUI(null)  // Başarısız girişte UI'yi güncelle
                    }
                }
        }
    }
}

