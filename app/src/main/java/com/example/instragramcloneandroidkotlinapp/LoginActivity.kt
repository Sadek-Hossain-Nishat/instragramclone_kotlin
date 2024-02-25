package com.example.instragramcloneandroidkotlinapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instragramcloneandroidkotlinapp.Models.User
import com.example.instragramcloneandroidkotlinapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.logInBtn.setOnClickListener {
            if ((binding.email.editText?.text.toString() == "") or
                (binding.pass.editText?.text.toString() == "")
            ) {
                Toast.makeText(this@LoginActivity, "Please fill all the details", Toast.LENGTH_LONG)
                    .show()

            } else {
                var user = User(
                    binding.email.editText?.text.toString(),
                    binding.pass.editText?.text.toString()
                )

                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {

                            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                            finish()


                        } else {
                            Toast.makeText(this@LoginActivity, it.exception?.localizedMessage,
                                Toast.LENGTH_LONG).show()

                        }
                    }
            }
        }
    }
}