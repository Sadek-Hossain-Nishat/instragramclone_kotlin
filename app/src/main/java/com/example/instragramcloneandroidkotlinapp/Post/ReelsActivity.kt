package com.example.instragramcloneandroidkotlinapp.Post

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instragramcloneandroidkotlinapp.HomeActivity
import com.example.instragramcloneandroidkotlinapp.Models.Post
import com.example.instragramcloneandroidkotlinapp.Models.Reel
import com.example.instragramcloneandroidkotlinapp.Models.User
import com.example.instragramcloneandroidkotlinapp.R
import com.example.instragramcloneandroidkotlinapp.databinding.ActivityReelsBinding
import com.example.instragramcloneandroidkotlinapp.utils.ALL_INSTRAGRAM_REEL
import com.example.instragramcloneandroidkotlinapp.utils.POST
import com.example.instragramcloneandroidkotlinapp.utils.POST_FOLDER
import com.example.instragramcloneandroidkotlinapp.utils.REEL
import com.example.instragramcloneandroidkotlinapp.utils.REEL_FOLDER
import com.example.instragramcloneandroidkotlinapp.utils.USER_NODE
import com.example.instragramcloneandroidkotlinapp.utils.uploadImage
import com.example.instragramcloneandroidkotlinapp.utils.uploadVideo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ReelsActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityReelsBinding.inflate(layoutInflater)
    }

    private lateinit var videoUrl:String

    private lateinit var progressDialog: ProgressDialog

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {

            uploadVideo(it,REEL_FOLDER,progressDialog) { url ->
                if (url != null) {


                    videoUrl = url

                }
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this@ReelsActivity)
        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }


        binding.cancelButton.setOnClickListener {

            startActivity( Intent(this@ReelsActivity, HomeActivity::class.java))

            finish()


        }


        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                var user:User = it.toObject<User>()!!

                val reel: Reel = Reel(videoUrl!!, binding.caption.editText?.text.toString(),user.image!!)
                Firebase.firestore.collection("${REEL}/*/${Firebase.auth.currentUser!!.uid}").document().set(reel).addOnSuccessListener {

                    Firebase.firestore.collection(ALL_INSTRAGRAM_REEL).document().set(reel).addOnSuccessListener {


                        startActivity( Intent(this@ReelsActivity, HomeActivity::class.java))

                        finish()



                    }




                }
            }


        }




    }
}