package com.webomax.openai.Profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.webomax.openai.R
import com.webomax.openai.presentation.MainActivity

class RegisterActivity : AppCompatActivity() {
    companion object{
        private const val RC_SIGN_IN = 120
    }
    lateinit var btn_register : CardView
    lateinit var email_regster : EditText
    lateinit var pass_register : EditText
    lateinit var name_register :EditText
    lateinit var googlebtn:TextView
    private var tvNameError: TextView? = null
    private var tvEmailError: TextView? = null
    private var tvPasswordError: TextView? = null
    private var tvColor: TextView? = null
    private var frameOne: CardView? = null
    private var frameTwo: CardView? = null
    private var frameThree: CardView? = null
    private var frameFour: CardView? = null
    private var isAtLeast8 = false
    private var hasUppercase = false
    private var hasNumber = false
    private var hasSymbol = false
    private var isRegistrationClickable = false
    lateinit var txt_login : TextView
    private lateinit var auth :FirebaseAuth
    var DatabaseReference :DatabaseReference? = null
    var Database : FirebaseDatabase? = null
    private lateinit var googleSignInClient: GoogleSignInClient


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
       frameOne = findViewById(R.id.frameOne)
        frameTwo = findViewById(R.id.frameTwo)
        frameThree = findViewById(R.id.frameThree)
        frameFour = findViewById(R.id.frameFour)
        tvColor = findViewById(R.id.tvColor)
        tvNameError = findViewById(R.id.tvNameError)
        tvEmailError = findViewById(R.id.tvEmailError)
        tvPasswordError = findViewById(R.id.tvPasswordError)
        email_regster = findViewById(R.id.emailregister)
        btn_register = findViewById(R.id.Registerbtn)
        pass_register = findViewById(R.id.passwordregister)
        name_register = findViewById(R.id.Nameregister)
        txt_login = findViewById(R.id.Logintxt)
        auth = FirebaseAuth.getInstance()
        Database = FirebaseDatabase.getInstance()
        DatabaseReference = Database?.reference!!.child("profile")
        googlebtn = findViewById(R.id.googlebtn_register)
        inputChange()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString((com.firebase.ui.auth.R.string.default_web_client_id)))
            .requestEmail()
            .build()
        googleSignInClient =GoogleSignIn.getClient(this,gso)

        googlebtn.setOnClickListener {
            signInGoogle()
        }

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

    @SuppressLint("ResourceType")
    private fun checkAllData(email: String) {
        if (isAtLeast8 && hasUppercase && hasNumber && hasSymbol && email.length > 0) {
            isRegistrationClickable = true
            tvColor!!.setTextColor(Color.WHITE)
            btn_register.setCardBackgroundColor(Color.parseColor(getString(R.color.teal_700)))
        } else {
            isRegistrationClickable = false
            btn_register!!.setCardBackgroundColor(Color.parseColor(getString(R.color.white)))
        }
    }
    @SuppressLint("ResourceType")
    private fun registrationDataCheck() {
        val password = pass_register!!.text.toString()
        val email = email_regster!!.text.toString()
        val name = name_register!!.text.toString()
        checkEmpty(name, email, password)
        if (password.length >= 8) {
            isAtLeast8 = true
            frameOne!!.setBackgroundColor(Color.parseColor(getString(R.color.teal_700)))
        } else {
            isAtLeast8 = false
            frameOne!!.setBackgroundColor(Color.parseColor(getString(R.color.white)))
        }
        if (password.matches(Regex(".*[A-Z].*"))) {
            hasUppercase = true
            frameTwo!!.setBackgroundColor(Color.parseColor(getString(R.color.teal_700)))
        } else {
            hasUppercase = false
            frameTwo!!.setBackgroundColor(Color.parseColor(getString(R.color.white)))
        }
        if (password.matches(Regex("(.*[0-9].*)"))) {
            hasNumber = true
            frameThree!!.setBackgroundColor(Color.parseColor(getString(R.color.teal_700)))
        } else {
            hasNumber = false
            frameThree!!.setBackgroundColor(Color.parseColor(getString(R.color.white)))
        }
        if (password.matches(Regex("^(?=.*[_.@()]).*$"))) {
            hasSymbol = true
            frameFour!!.setBackgroundColor(Color.parseColor(getString(R.color.teal_700)))
        } else {
            hasSymbol = false
            frameFour!!.setBackgroundColor(Color.parseColor(getString(R.color.white)))
        }
        checkAllData(email)
    }
    private fun checkEmpty(name: String, email: String, password: String) {
        if (name.length > 0 && tvNameError!!.visibility == View.VISIBLE) {
            tvNameError!!.visibility = View.GONE
        }
        if (password.length > 0 && tvPasswordError!!.visibility == View.VISIBLE) {
            tvPasswordError!!.visibility = View.GONE
        }
        if (email.length > 0 && tvEmailError!!.visibility == View.VISIBLE) {
            tvEmailError!!.visibility = View.GONE
        }
    }
    private fun inputChange() {
        email_regster!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                registrationDataCheck()
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        pass_register!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                registrationDataCheck()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RegisterActivity.RC_SIGN_IN)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RegisterActivity.RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try{
                    val account= task.getResult(ApiException::class.java)!!
                    Log.d("loginActivity","firebaswAuthWithGoogle:"+ account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                }catch (e: ApiException){
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
                    Log.d("RegisterActivity","signwithCredential:success")
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Log.w("RegisterActivity","signInwithCredential:failure",task.exception)
                }

            }
    }
}