package com.webomax.openai.Profile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.webomax.openai.R
import com.webomax.openai.presentation.MainActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var mAuth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN

        )

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser



        Handler().postDelayed({
            if(user !=null){
                val MainActivity = Intent(this,MainActivity::class.java)
                startActivity(MainActivity)
                finish()
            }
            else{
                val loginActivity =  Intent(this, loginActivity::class.java)
                startActivity(loginActivity)
                finish()
            }
        },4000)
    }
}