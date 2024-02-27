package com.example.instragramcloneandroidkotlinapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instragramcloneandroidkotlinapp.Models.Post
import com.example.instragramcloneandroidkotlinapp.Models.User
import com.example.instragramcloneandroidkotlinapp.R
import com.example.instragramcloneandroidkotlinapp.databinding.PostRvBinding
import com.example.instragramcloneandroidkotlinapp.utils.USER_NODE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class PostAdapter(var context:Context,var postList:ArrayList<Post>):RecyclerView.Adapter<PostAdapter.MyHolder> (){

    inner class MyHolder(binding:PostRvBinding):RecyclerView.ViewHolder(binding.root){



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = PostRvBinding.inflate(LayoutInflater.from(context),parent,false)

        return  MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        Firebase.firestore.collection(USER_NODE).document(postList.get(position).uid).get().addOnSuccessListener {


            var user = it.toObject<User>()

            Glide.with(context).load(user!!.image).placeholder(R.drawable.user)



        }

    }
}