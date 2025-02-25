package com.tayyipgunay.instaclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tayyipgunay.instaclone.databinding.RecyclerRowBinding
import com.tayyipgunay.instaclone.model.Post
import java.util.ArrayList

class FeedRecyclerAdapter(val postList: ArrayList<Post>): RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {


class PostHolder(val  binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)

    }

    override fun getItemCount(): Int {
       return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
      holder.binding.userEmailid.text=postList.get(position).email
        holder.binding.recyclerCommentTextid.text=postList.get(position).comment
        Picasso.get()
            .load(postList.get(position).downloadUrl).into(holder.binding.RecyclerImageViewid)
                /*Neden kullanıyoruz?
        Kolaylık: Picasso, tek satır kodla ağdan resim indirip uygulamaya yerleştirmenizi sağlar.
        Verimlilik: Picasso, görüntüleri önbelleğe alır, böylece aynı resimleri tekrar tekrar indirmenize gerek kalmaz. Ayrıca, otomatik olarak görüntüleri sıkıştırır ve boyutlandırır.
        Arka Plan İşlemleri: Picasso, asenkron olarak çalışır; yani resim indirildiğinde uygulamanız donmaz, kullanıcı deneyimi olumsuz etkilenmez.*/




    }

}