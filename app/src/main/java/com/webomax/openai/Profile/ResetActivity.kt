package com.webomax.openai.Profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.webomax.openai.R

class ResetActivity : AppCompatActivity() {
    lateinit var rest_btn:Button
    lateinit var Email_txt:EditText
    lateinit var auth: FirebaseAuth
    lateinit var back_btn:ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        rest_btn=findViewById(R.id.Reset_btn)
        Email_txt =findViewById(R.id.EmailAddress)
        back_btn=findViewById(R.id.backbutton_rest)
        auth=FirebaseAuth.getInstance()

        rest_btn.setOnClickListener{
            when {

                TextUtils.isEmpty(Email_txt.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@ResetActivity,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val sPassword: String = Email_txt.text.toString().trim() { it <= ' ' }
                    auth.sendPasswordResetEmail(sPassword)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Please Check your Email", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
                        }

                }
            }

        }
        back_btn.setOnClickListener{
            startActivity(Intent(this@ResetActivity, loginActivity::class.java))
            finish()
        }


    }
}