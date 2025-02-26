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
    private lateinit var auth: FirebaseAuth // Firebase kimlik doğrulama nesnesi
    private var firebaseUser: FirebaseUser? = null // Mevcut oturum açmış kullanıcıyı tutan değişken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Authentication başlatılıyor
        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser // Mevcut oturum açmış kullanıcı alınıyor
        updateUI(firebaseUser) // UI güncelleniyor
    }

    // Kullanıcının giriş yapıp yapmadığını kontrol eden fonksiyon
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Kullanıcı oturum açtıysa FeedActivity'ye yönlendir
            val intent = Intent(this@MainActivity, FeedActivitiy::class.java)
            startActivity(intent)
            finish() // MainActivity'yi kapatıyoruz
        } else {
            // Kullanıcı oturum açmamışsa giriş sayfasında kalır
            Toast.makeText(this@MainActivity, "Lütfen giriş yapın", Toast.LENGTH_LONG).show()
        }
    }

    // Kullanıcı kayıt olma butonuna bastığında çalışan fonksiyon
    fun signUpClicked(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@MainActivity, "Lütfen boş yerleri doldurun", Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                // Kayıt başarılı olursa FeedActivity'ye yönlendir
                val intent = Intent(this@MainActivity, FeedActivitiy::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    // Kullanıcı giriş yapma butonuna bastığında çalışan fonksiyon
    fun signInClicked(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@MainActivity, "Lütfen boş yerleri doldurun", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        firebaseUser = auth.currentUser // Giriş başarılı olursa oturum açan kullanıcıyı güncelle
                        updateUI(firebaseUser) // UI'yi güncelle
                    } else {
                        Toast.makeText(this@MainActivity, "Giriş Başarısız", Toast.LENGTH_LONG).show()
                        updateUI(null) // Başarısız girişte UI'yi güncelle
                    }
                }
        }
    }
}
