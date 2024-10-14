package com.example.insstagramm

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.insstagramm.Models.User
import com.example.insstagramm.databinding.ActivitySignupBinding
import com.example.insstagramm.utils.USER_NODE
import com.example.insstagramm.utils.USER_PROFILE_FOLDER
import com.example.insstagramm.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //In short, the binding property is your Kotlin code's gateway to interact with the views defined in the activity_signup.xml
        // layout. It makes your code cleaner, safer, and more efficient.
        val binding by lazy {
            ActivitySignupBinding.inflate(layoutInflater)
        }
        val text = "<font color=black>Already have an Account </font> <font color=blue>login?</font>"
        binding.login.setText(Html.fromHtml(text))
         val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let{
                uploadImage(uri, USER_PROFILE_FOLDER){
                    if(it==null){

                    }else{
                        user.image=it
                        binding.imageView2.setImageURI(uri)
                    }
                }
            }
        }
        enableEdgeToEdge()
        setContentView(binding.root)
        user = User()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.signUpBtn.setOnClickListener {
            if (binding.name.text.toString().isEmpty() ||
                binding.Email.text.toString().isEmpty() ||
                binding.Password.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this@SignupActivity,
                    "Please fill all required information",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.Email.text.toString(),
                    binding.Password.text.toString()

                ).addOnCompleteListener { result ->

                    if (result.isSuccessful) {
                        user.name = binding.name.text.toString()
                        user.email = binding.Email.text.toString()
                        user.password = binding.Password.text.toString()
                        Firebase.firestore.collection(USER_NODE)
                            .document(Firebase.auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                startActivity(Intent(this@SignupActivity, HomeActivity::class.java))
                                finish()
                            }

                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            result.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        binding.imageView2.setOnClickListener{
            launcher.launch("image/*")
        }
        binding.login.setOnClickListener{
            startActivity(Intent(this@SignupActivity, loginActivity::class.java))
        }
    }
}
// creating the login page and understand the image uploading process