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

    private lateinit var auth: FirebaseAuth // Kullanıcı kimlik doğrulama için FirebaseAuth nesnesi
    private lateinit var storage: FirebaseStorage // Firebase Storage kullanımı için nesne
    private lateinit var db: FirebaseFirestore // Firestore veritabanı işlemleri için nesne

    var selectedPicture: Uri? = null // Kullanıcının seçtiği resmin URI bilgisi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadActivitiyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerLauncher() // Galeri ve izin işlemlerini yönetecek launcher'ları kaydet

        auth = FirebaseAuth.getInstance() // Firebase kimlik doğrulama başlat
        storage = FirebaseStorage.getInstance() // Firebase Storage başlat
        db = Firebase.firestore // Firestore başlat
    }

    // Resim yükleme işlemini başlatan fonksiyon
    fun upload(view: View) {
        try {
            val uuid = UUID.randomUUID() // Her resme benzersiz bir kimlik atamak için UUID oluştur
            val imageaName = "$uuid.jpg" // Dosya adı olarak UUID kullan
            val reference = storage.reference // Firebase Storage referansı al

            selectedPicture?.let { selectedPicture ->
                val imageReference = reference.child("images").child(imageaName) // Firebase'de 'images' klasörü altında sakla

                imageReference.putFile(selectedPicture)
                    .addOnSuccessListener {
                        val uploadPicureReference = reference.child("images").child(imageaName)

                        // Yüklenen dosyanın URL'sini al
                        uploadPicureReference.downloadUrl.addOnSuccessListener { Url ->
                            Toast.makeText(this@UploadActivitiy, "Yükleme başarılı", Toast.LENGTH_LONG).show()

                            val downloadUrl = Url.toString() // URL'yi string olarak al

                            // Firestore veritabanına yüklenecek verileri içeren HashMap
                            val postMap = hashMapOf<String, Any>()
                            postMap.put("downloadUrl", downloadUrl)
                            postMap.put("userEmail", auth.currentUser!!.email!!)
                            postMap.put("comment", binding.commentid.text.toString())
                            postMap.put("date", Timestamp.now())

                            // Firestore'da 'Posts' koleksiyonuna belge ekle
                            db.collection("Posts").add(postMap)
                                .addOnSuccessListener {
                                    finish() // Yükleme ve veri tabanı işlemi başarılıysa activity'yi kapat
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@UploadActivitiy, it.localizedMessage, Toast.LENGTH_LONG).show()
                                }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this@UploadActivitiy, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            }
        } catch (e: Exception) {
            println("Yükleme başarısız: " + e.printStackTrace())
        }
    }

    // Kullanıcının galeriye erişip resim seçmesini sağlayan fonksiyon
    fun selectImage(view: View) {
        if (ContextCompat.checkSelfPermission(this@UploadActivitiy, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) // Eğer izin verilmediyse, izin iste
        } else {
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            ActivityResultLauncher.launch(intentToGallery) // Kullanıcının galeriden resim seçmesini sağla
        }
    }

    // Resim seçimi ve izin işlemlerini yönetecek launcher'ları kaydeden fonksiyon
    private fun registerLauncher() {
        ActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) { // Kullanıcı bir resim seçmişse
                    val imageDataUri = result.data?.data
                    if (imageDataUri != null) {
                        binding.imageView.setImageURI(imageDataUri) // Seçilen resmi ImageView'e göster
                        selectedPicture = imageDataUri // Seçilen resmi sakla
                    }
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    ActivityResultLauncher.launch(intentToGallery) // Kullanıcı izin verdiyse galeri aç
                } else {
                    Toast.makeText(this@UploadActivitiy, "İzin Reddedildi", Toast.LENGTH_LONG).show()
                }
            }
    }
}
