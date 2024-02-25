package com.example.instragramcloneandroidkotlinapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instragramcloneandroidkotlinapp.Models.Post
import com.example.instragramcloneandroidkotlinapp.Models.Reel
import com.example.instragramcloneandroidkotlinapp.adapters.MyPostRvAdapter
import com.example.instragramcloneandroidkotlinapp.adapters.MyReelAdapter
import com.example.instragramcloneandroidkotlinapp.databinding.FragmentMyReelsBinding
import com.example.instragramcloneandroidkotlinapp.utils.REEL
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MyReelsFragment : Fragment() {

    private lateinit var binding:FragmentMyReelsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentMyReelsBinding.inflate(inflater, container, false)
        var reelList = ArrayList<Reel>()
        var adapter = MyReelAdapter(requireContext(),reelList)
        binding.rv.layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter
        Firebase.firestore.collection("$REEL/*/${Firebase.auth.currentUser!!.uid}")
            .get().addOnSuccessListener {

                var tempList = arrayListOf<Reel>()

                for (i in it.documents) {
                    var reel: Reel = i.toObject<Reel>()!!
                    tempList.add(reel)
                }

                reelList.addAll(tempList)
                adapter.notifyDataSetChanged()

            }
        return binding.root
    }

    companion object {


    }
}