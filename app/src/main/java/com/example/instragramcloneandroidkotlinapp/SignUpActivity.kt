package com.example.instragramcloneandroidkotlinapp


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instragramcloneandroidkotlinapp.Models.User
import com.example.instragramcloneandroidkotlinapp.databinding.ActivitySignUpBinding
import com.example.instragramcloneandroidkotlinapp.utils.USER_NODE
import com.example.instragramcloneandroidkotlinapp.utils.USER_PROFILE_FOLDER
import com.example.instragramcloneandroidkotlinapp.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class SignUpActivity : AppCompatActivity() {

    val binding by lazy {

        ActivitySignUpBinding.inflate(layoutInflater)

    }

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {

            uploadImage(it, USER_PROFILE_FOLDER) {
                if (it != null) {

                    user.image = it
                    binding.profileImage.setImageURI(uri)

                }
            }

        }
    }

    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text =
            "<font color=#ff000000>Already have an Account?</font><font color=#1e88e5>Login</font>"
        binding.login.text = Html.fromHtml(text)
        setContentView(binding.root)

        user = User()

        if (intent.hasExtra("MODE")) {
            if (intent.getIntExtra("MODE", -1) == 1) {
                binding.signUpBtn.text = "Update Profile"

                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                    .get().addOnSuccessListener {
                        user = it.toObject<User>()!!

                        if (!user.image.isNullOrEmpty()) {


                            Picasso.get().load(user.image).into(binding.profileImage)


                        }
                        binding.name.editText?.setText(user.name)
                        binding.email.editText?.setText(user.email)
                        binding.password.editText?.setText(user.password)


                    }
            }
        }

        binding.signUpBtn.setOnClickListener {
            if (intent.hasExtra("MODE")) {
                if (intent.getIntExtra("MODE", -1) == 1){



                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid)
                        .set(user)

                        .addOnSuccessListener {

                            startActivity(
                                Intent(this@SignUpActivity, HomeActivity::class.java)

                            )

                            finish()





                        }




                }
            } else {



                if (binding.name.editText?.text.toString().equals("") or
                    binding.email.editText?.text.toString().equals("") or
                    binding.password.editText?.text.toString().equals("")
                ) {


                    Toast.makeText(
                        this@SignUpActivity,
                        "Please fill all information",
                        Toast.LENGTH_LONG
                    ).show()

                } else {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(

                        binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString()

                    )
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {

                                user.name = binding.name.editText?.text.toString()
                                user.email = binding.email.editText?.text.toString()
                                user.password = binding.password.editText?.text.toString()
                                Firebase.firestore.collection(USER_NODE)
                                    .document(Firebase.auth.currentUser!!.uid)
                                    .set(user)
                                    .addOnSuccessListener {

                                        startActivity(
                                            Intent(this@SignUpActivity, HomeActivity::class.java)

                                        )

                                        finish()




                                        Toast.makeText(
                                            this@SignUpActivity,
                                            "Login Successfull",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    }


                            } else {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    result.exception?.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                }



            }

        }

        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.login.setOnClickListener {

            startActivity(
                Intent(this@SignUpActivity, LoginActivity::class.java)


            )
            finish()

        }


    }
}