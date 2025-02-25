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
    private lateinit var auth: FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private lateinit var  postArrayList:ArrayList<Post>
    private lateinit var feedRecyclerAdapter: FeedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFeedActivitiyBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()
        db=FirebaseFirestore.getInstance()
        postArrayList=ArrayList<Post>()

        getData()
        binding.RecyclerView.layoutManager=LinearLayoutManager(this@FeedActivitiy)

        feedRecyclerAdapter=FeedRecyclerAdapter(postArrayList)
        binding.RecyclerView.adapter=feedRecyclerAdapter

        /* enableEdgeToEdge()
         setContentView(R.layout.activity_feed_activitiy)
         ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
             val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
             v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
             insets
         }*/
    }
    private  fun getData(){
db.collection("Posts").orderBy("date", Query.Direction.DESCENDING)
    .addSnapshotListener { value, error ->
     if(error!=null){
         Toast.makeText(this@FeedActivitiy,error.localizedMessage,Toast.LENGTH_LONG).show()
     }
    if(value!=null){//değer null i değil ise
        if(!value.isEmpty){//döküman olmayabilir.


          /// val documents= value.documents



             postArrayList.clear()
            for(document in value){
                // Belgeden "comment" alanını doğrudan alır ve String olarak dönüştürür
                val comment = document.get("comment") as String

//  "document.get" metodu, "comment" alanına doğrudan erişir.
// Performans açısından daha verimlidir çünkü sadece gerekli alanı alır.
// Ancak, eğer "comment" alanı String değilse, bu kod çalışırken hata verebilir.


/* Belgenin tüm verilerini bir Map olarak alır ve "comment" alanını bu Map'ten çeker
                val a = document.data.get("comment") as String
 Yorum: "document.data" önce belgenin tüm alanlarını bir Map yapısına yükler.
 Daha sonra "comment" alanını bu Map'ten alır.
 Performans olarak biraz daha maliyetlidir çünkü tüm veriler Map'e dönüştürülür.
 Aynı şekilde, "comment" alanı String değilse, burada da çalışma zamanı hatası alabilirsiniz.

*/
              val useremail=document.get("userEmail")as String
              val downloadUrl=document.get("downloadUrl")as String
           //   val date=document.getDate("Date")
                val post= Post(useremail,comment,downloadUrl)
                postArrayList.add(post)


//tarihe nereden ulaştık.


          }
            feedRecyclerAdapter.notifyDataSetChanged()

        }
    }



}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=getMenuInflater()
        menuInflater.inflate(R.menu.instamenu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.add_post){
            val intent=Intent(this@FeedActivitiy, UploadActivitiy::class.java)
            startActivity(intent)

        }
        else if (item.itemId== R.id.Signout){
            auth.signOut()
            val intent=Intent(this@FeedActivitiy, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        return super.onOptionsItemSelected(item)
    }

}