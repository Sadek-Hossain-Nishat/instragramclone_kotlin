package com.example.instragramcloneandroidkotlinapp.Post

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instragramcloneandroidkotlinapp.HomeActivity
import com.example.instragramcloneandroidkotlinapp.Models.Post
import com.example.instragramcloneandroidkotlinapp.Models.User
import com.example.instragramcloneandroidkotlinapp.databinding.ActivityPostBinding
import com.example.instragramcloneandroidkotlinapp.utils.ALL_INSTRAGRAM_POST
import com.example.instragramcloneandroidkotlinapp.utils.POST
import com.example.instragramcloneandroidkotlinapp.utils.POST_FOLDER
import com.example.instragramcloneandroidkotlinapp.utils.USER_NODE
import com.example.instragramcloneandroidkotlinapp.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl: String? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {

            uploadImage(it, POST_FOLDER) { url ->
                if (url != null) {

                    binding.selectImage.setImageURI(uri)
                    imageUrl = url

                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
            finish()
        }

        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }


        binding.cancelButton.setOnClickListener {

            startActivity(Intent(this@PostActivity, HomeActivity::class.java))

            finish()


        }

        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(
//                Firebase.auth.currentUser!!.uid
            ).get()
                .addOnSuccessListener {
                    var user = it.toObject<User>()

                    val post: Post = Post(
                        postUrl = imageUrl!!,
                        caption = binding.caption.editText?.text.toString(),
//                        name = user!!.name.toString(),
                        uid = Firebase.auth.currentUser!!.uid,
                        time = System.currentTimeMillis().toString()
                    )


                    Firebase.firestore.collection("${POST}/*/${Firebase.auth.currentUser!!.uid}")
                        .document().set(post).addOnSuccessListener {

                            Firebase.firestore.collection(ALL_INSTRAGRAM_POST).document().set(post)
                                .addOnSuccessListener {

                                    startActivity(
                                        Intent(
                                            this@PostActivity,
                                            HomeActivity::class.java
                                        )
                                    )

                                    finish()


                                }


                        }


                }


        }
    }
}