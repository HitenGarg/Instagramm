package com.example.insstagramm

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.insstagramm.Models.User
import com.example.insstagramm.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding by lazy {
            ActivityLoginBinding.inflate(layoutInflater)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.loginBtn.setOnClickListener{
            if(binding.Email.text.toString().isEmpty()||
                binding.Password.text.toString().isEmpty()){
                Toast.makeText(this@loginActivity, "Fill all the information", Toast.LENGTH_SHORT).show()
            }else{
                var user=User(binding.Email.text.toString(), binding.Password.text.toString())

                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            startActivity(Intent(this@loginActivity, HomeActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this@loginActivity, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.createAccount.setOnClickListener{
            startActivity(Intent(this@loginActivity, SignupActivity::class.java))
        }
    }
}