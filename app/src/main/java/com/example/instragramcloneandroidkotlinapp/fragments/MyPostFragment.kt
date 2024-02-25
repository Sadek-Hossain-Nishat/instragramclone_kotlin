package com.example.instragramcloneandroidkotlinapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instragramcloneandroidkotlinapp.Models.Post
import com.example.instragramcloneandroidkotlinapp.R
import com.example.instragramcloneandroidkotlinapp.adapters.MyPostRvAdapter
import com.example.instragramcloneandroidkotlinapp.databinding.FragmentMyPostBinding
import com.example.instragramcloneandroidkotlinapp.utils.POST
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MyPostFragment : Fragment() {

    private lateinit var binding: FragmentMyPostBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentMyPostBinding.inflate(inflater, container, false)
        var postList = ArrayList<Post>()
        var adapter = MyPostRvAdapter(requireContext(),postList)
        binding.rv.layoutManager= StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter
        Firebase.firestore.collection("${POST}/*/${Firebase.auth.currentUser!!.uid}")
            .get().addOnSuccessListener {

                var tempList = arrayListOf<Post>()

                for (i in it.documents) {
                    var post:Post = i.toObject<Post>()!!
                    tempList.add(post)
                }

                postList.addAll(tempList)
                adapter.notifyDataSetChanged()

            }
        return binding.root
    }

    companion object {


    }
}