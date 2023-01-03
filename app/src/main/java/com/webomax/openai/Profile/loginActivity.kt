package com.webomax.openai.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.webomax.openai.R
import com.webomax.openai.presentation.MainActivity


class loginActivity : AppCompatActivity() {
    companion object{
        private const val RC_SIGN_IN = 120
    }
     lateinit var txt_register :TextView
     lateinit var btn_login:Button
     lateinit var txt_email :EditText
     lateinit var txt_pass :EditText
     lateinit var google_btn : TextView
     private lateinit var googleSignInClient: GoogleSignInClient
     lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txt_register = findViewById(R.id.Registertxt)
        btn_login = findViewById(R.id.loginbtn)
        txt_email = findViewById(R.id.emaillogin)
        txt_pass = findViewById(R.id.passwordlogin)
        google_btn = findViewById(R.id.googlebtn)


       val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestIdToken(getString((com.firebase.ui.auth.R.string.default_web_client_id)))
           .requestEmail()
           .build()
        googleSignInClient =GoogleSignIn.getClient(this,gso)
         auth= FirebaseAuth.getInstance()
        val currentuser  = auth.currentUser
        if (currentuser != null) {
            startActivity(Intent(this@loginActivity,MainActivity::class.java))
            finish()
        }

        google_btn.setOnClickListener{
            signInGoogle()
        }

        txt_register.setOnClickListener {
            startActivity(Intent(this@loginActivity, RegisterActivity::class.java))
            finish()
        }
        btn_login.setOnClickListener {
            when {
                TextUtils.isEmpty(txt_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@loginActivity,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(txt_pass.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@loginActivity,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = txt_email.text.toString().trim() { it <= ' ' }
                    val password: String = txt_pass.text.toString().trim() { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@loginActivity,
                                    "You were login successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()


                                val intent =
                                    Intent(this@loginActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(
                                    "user_id",
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                )
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@loginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }


                }

            }

        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try{
                    val account= task.getResult(ApiException::class.java)!!
                    Log.d("loginActivity","firebaswAuthWithGoogle:"+ account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                }catch (e:ApiException){
                    Log.w("loginActivity","Google sign in Failed",e)
                }
            }else{
                Log.w("loginActivity",exception.toString())
            }

        }
    }
    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener (this){ task ->
                if(task.isSuccessful) {
                    Log.d("loginActivity","signwithCredentila:success")
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Log.w("loginActivity","signInwithCredential:failure",task.exception)
                }

            }
    }

}