package com.tayyipgunay.instaclone.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.tayyipgunay.instaclone.databinding.ActivityUploadActivitiyBinding
import java.util.UUID

class UploadActivitiy : AppCompatActivity() {
    private lateinit var binding: ActivityUploadActivitiyBinding
    private lateinit var ActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    //private lateinit var selectedPicture: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseFirestore
    ///var reference=storage.reference //


    var selectedPicture: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadActivitiyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        selectedPicture = null
        db = Firebase.firestore
        //reference=storage.reference


    }
    /* enableEdgeToEdge()
     setContentView(R.layout.activity_upload_activitiy)
     ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
         val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
         v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
         insets
     }*/

    fun upload(view: View) {
        try {
            val uuid = UUID.randomUUID()
            val imageaName = "$uuid.jpg"
            val reference = storage.reference

            // reference.putFile(selectedPicture)
            //val imageReference = reference.child("images/$imageaName")
            val imageReference = reference.child("images").child(imageaName)//images içerisinde farklı resim idler,
            // if(selectedPicture!=null){// gerek duyuluyorsa yapılabilir.
            selectedPicture?.let { selectedPicture ->

                imageReference.putFile(selectedPicture)

                    .addOnSuccessListener {//dosya yükleme ve dosya yükleme başarılı ise olacaklar.

                        val uploadPicureReference = reference.child("images").child(imageaName)

                        uploadPicureReference.downloadUrl.addOnSuccessListener { Url->//eğer bir indirme url varsa yükleme başarılı demektir.
                            Toast.makeText(
                                this@UploadActivitiy,
                                "yükleme başarılı",
                                Toast.LENGTH_LONG
                            ).show()
                            //resim indirme url ve diğer bilgileri alıp  postMap kaydedip
                            // ordan veritabanına ekliyoruz.
                            val downloadUrl = Url.toString()// indirme url aldık.
                            //  if(auth.currentUser!=null){//eğer kullanıcı oturum açmadan tuş olursa bunu kullanırız.

                            val postMap = hashMapOf<String, Any>()//veriTabanına yükleme işlemine başlıyoruz
                            postMap.put("downloadUrl", downloadUrl)//uri.ToString()

                            postMap.put("userEmail", auth.currentUser!!.email!!)
                            postMap.put("comment", binding.commentid.text.toString())
                            postMap.put("date", Timestamp.now())
                            //db.collection("Posts").document("A").set(postMap)//belirli dosyayı güncellmek için olabilir.eklemek için de olabilir.
                               db.collection("Posts").add(postMap)


                                .addOnSuccessListener {//veriTabanına yükleme.
                                    //hem yükleme hem indirme başarılı ise  activity kapat.


                                    finish()

                                }.addOnFailureListener {
                                    Toast.makeText(
                                        this@UploadActivitiy,
                                        it.localizedMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            // }
                        }
                        //dowloadn url-ZfireStore
                    }.addOnFailureListener {
                        Toast.makeText(this@UploadActivitiy, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()

                    }
            }
        }
        // }

        catch (e: Exception) {
            println("OLMADI!!  " + e.printStackTrace())
        }
    }

    fun selectImage(view: View) {
        if (ContextCompat.checkSelfPermission(this@UploadActivitiy, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            //izin reddedildi
        } else {
            //izin var
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            ActivityResultLauncher.launch(intentToGallery)
        }

    }

    private fun registerLauncher() {

        val intent = intent

        ActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == RESULT_OK) {//resim seçilmişse

                    val imageDataUri = result.data?.data
                    if (imageDataUri != null) {
                        binding.imageView.setImageURI(imageDataUri)//bitmap ihtiyaç yok uri üzerinden upload edeceğiz
                        selectedPicture = imageDataUri
                    }
                }


            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    ActivityResultLauncher.launch(intentToGallery)
                } else {
                    Toast.makeText(this@UploadActivitiy, "İzin Reddedildi", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }


}