package com.tayyipgunay.instaclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tayyipgunay.instaclone.databinding.RecyclerRowBinding
import com.tayyipgunay.instaclone.model.Post
import java.util.ArrayList

class FeedRecyclerAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {

    // RecyclerView'daki her öğe için ViewHolder tanımlaması
    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder oluşturma işlemi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    // Liste öğe sayısını döndürür
    override fun getItemCount(): Int {
        return postList.size
    }

    // ViewHolder'ı, liste verileriyle bağlayan fonksiyon
    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.userEmailid.text = postList[position].email // Kullanıcı e-postasını ata
        holder.binding.recyclerCommentTextid.text = postList[position].comment // Yorumu ata

        // Resmi yükleyip ImageView'e atar
        Picasso.get()
            .load(postList[position].downloadUrl)
            .into(holder.binding.RecyclerImageViewid)

        /* Picasso'nun kullanılma sebepleri:
        - **Kolaylık**: Tek satır kod ile ağdan resim indirip uygulamaya yerleştirmeyi sağlar.
        - **Verimlilik**: Görselleri önbelleğe alarak tekrar tekrar indirilmesini engeller ve görüntüleri otomatik olarak sıkıştırır/boyutlandırır.
        - **Arka Plan İşlemleri**: Asenkron çalışarak uygulamanın donmasını önler ve kullanıcı deneyimini iyileştirir. */
    }
}
