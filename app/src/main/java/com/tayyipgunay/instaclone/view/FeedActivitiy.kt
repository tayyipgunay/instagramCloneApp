package com.tayyipgunay.instaclone.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SnapshotMetadata
import com.tayyipgunay.instaclone.R
import com.tayyipgunay.instaclone.adapter.FeedRecyclerAdapter
import com.tayyipgunay.instaclone.databinding.ActivityFeedActivitiyBinding
import com.tayyipgunay.instaclone.model.Post

class FeedActivitiy : AppCompatActivity() {
    private lateinit var binding: ActivityFeedActivitiyBinding
    private lateinit var auth: FirebaseAuth // Firebase kimlik doğrulama nesnesi
    private lateinit var db: FirebaseFirestore // Firestore veritabanı işlemleri için nesne
    private lateinit var postArrayList: ArrayList<Post> // Gönderileri tutan liste
    private lateinit var feedRecyclerAdapter: FeedRecyclerAdapter // RecyclerView adaptörü

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedActivitiyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance() // Firebase kimlik doğrulama başlatılıyor
        db = FirebaseFirestore.getInstance() // Firestore başlatılıyor
        postArrayList = ArrayList<Post>() // Boş bir liste başlatılıyor

        getData() // Firestore'dan verileri al
        binding.RecyclerView.layoutManager = LinearLayoutManager(this@FeedActivitiy)

        feedRecyclerAdapter = FeedRecyclerAdapter(postArrayList)
        binding.RecyclerView.adapter = feedRecyclerAdapter
    }

    // Firestore'dan gönderileri çekmek için kullanılan fonksiyon
    private fun getData() {
        db.collection("Posts").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(this@FeedActivitiy, error.localizedMessage, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) { // Eğer belge boş değilse
                    postArrayList.clear() // Önce liste temizlenir
                    for (document in value) {
                        val comment = document.get("comment") as String // Yorum bilgisi
                        val useremail = document.get("userEmail") as String // Kullanıcı e-postası
                        val downloadUrl = document.get("downloadUrl") as String // Resim URL'si

                        val post = Post(useremail, comment, downloadUrl) // Post nesnesi oluştur
                        postArrayList.add(post) // Listeye ekle
                    }
                    feedRecyclerAdapter.notifyDataSetChanged() // Adapter'e değişiklik bildir
                }
            }
    }

    // Menü seçeneklerini oluşturma fonksiyonu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.instamenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Menü seçeneklerine tıklanınca yapılacak işlemler
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_post) {
            val intent = Intent(this@FeedActivitiy, UploadActivitiy::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.Signout) {
            auth.signOut() // Kullanıcı çıkış yapıyor
            val intent = Intent(this@FeedActivitiy, MainActivity::class.java)
            startActivity(intent)
            finish() // Activity kapatılıyor
        }
        return super.onOptionsItemSelected(item)
    }
}
