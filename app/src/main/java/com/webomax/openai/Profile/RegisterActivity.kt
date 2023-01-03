package com.webomax.openai.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.webomax.openai.R
import com.webomax.openai.presentation.MainActivity

class RegisterActivity : AppCompatActivity() {
    lateinit var btn_register : Button
    lateinit var email_regster : EditText
    lateinit var pass_register : EditText
    lateinit var name_register :EditText
    lateinit var txt_login : TextView
    private lateinit var auth :FirebaseAuth
    var DatabaseReference :DatabaseReference? = null
    var Database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        email_regster = findViewById<EditText>(R.id.emailregister)
        btn_register = findViewById<Button>(R.id.Registerbtn)
        pass_register = findViewById(R.id.passwordregister)
        name_register = findViewById(R.id.Nameregister)
        txt_login = findViewById(R.id.Logintxt)
        auth = FirebaseAuth.getInstance()
        Database = FirebaseDatabase.getInstance()
        DatabaseReference = Database?.reference!!.child("profile")

        txt_login.setOnClickListener {
            startActivity(Intent(this, loginActivity::class.java))
        }
        register()
    }



    private fun register(){
        btn_register.setOnClickListener {
            when {
                TextUtils.isEmpty(email_regster.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(pass_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                TextUtils.isEmpty(name_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter Name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else ->{

                    val email:String = email_regster.text.toString().trim{ it <= ' '}
                    val password:String = pass_register.text.toString().trim{ it <= ' '}
                    val name:String = pass_register.text.toString().trim{ it <= ' '}



                    auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser= task.result!!.user!!
                                val currentUserDb = DatabaseReference?.child(firebaseUser.uid!!)
                                currentUserDb?.child("name")?.setValue(name_register.text.toString())
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "You were registered successfully.", Toast.LENGTH_SHORT
                                ).show()

                                val intent
                                = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user id",firebaseUser.uid)
                                intent.putExtra("email_id",email)
                                intent.putExtra("name:",name)

                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }

        }
    }
}