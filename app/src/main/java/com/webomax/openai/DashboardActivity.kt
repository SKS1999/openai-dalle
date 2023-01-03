package com.webomax.openai

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.webomax.openai.databinding.ActivityDashboardBinding
import com.webomax.openai.presentation.MainActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class DashboardActivity : AppCompatActivity() {
    lateinit var id_txt : TextView
    lateinit var name_txt : TextView
    lateinit var email_txt : TextView
    lateinit var profile : CircleImageView
    lateinit var back_btn : ImageView
    lateinit var delete_btn : Button
    private lateinit var auth :FirebaseAuth
    private lateinit var dialog: AlertDialog
    private lateinit var storage:FirebaseStorage
    private lateinit var selectimg: Uri
    private lateinit var updateprofile_btn : Button
    lateinit var binding :ActivityDashboardBinding
    var DatabaseReference : DatabaseReference? = null
    var Database : FirebaseDatabase? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        binding = ActivityDashboardBinding.inflate (layoutInflater)
        setContentView(binding.root)

        id_txt = findViewById(R.id.textid)
        name_txt = findViewById(R.id.textname)
        email_txt = findViewById(R.id.textemail)
        profile = findViewById(R.id.UserImage)
        back_btn = findViewById(R.id.backbutton)
        delete_btn = findViewById(R.id.buttondelete)
        updateprofile_btn = findViewById(R.id.UpdateProfile)


        Database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        DatabaseReference = Database?.reference?.child("profile")

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        profile.setOnClickListener{
            val intent= Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }
        updateprofile_btn.setOnClickListener{
                updateProfile()

        }



        loadProfile()





        back_btn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        delete_btn.setOnClickListener {
            currentUser?.delete()?.addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(this,"Account is successfully removed.",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,RegisterActivity::class.java)
                    startActivity(intent)
                    finish()

                }else{
                    Log.e("error",it.exception.toString())

                }
            }

        }
    }
    private fun loadProfile(){

        val user = auth.currentUser
        val userreference =DatabaseReference?.child(user?.uid!!)
        id_txt.text =user?.uid
        email_txt.text= user?.email
        name_txt.text = user?.displayName

        userreference?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data !=null){
            if(data.data!=null){
                selectimg = data.data!!

                binding.UserImage.setImageURI(selectimg)
            }
            else{

            }
        }


    }
    private fun updateProfile(){
        auth.currentUser.let { user ->
            val UserName = name_txt.text.toString()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(UserName)
                .setPhotoUri(selectimg)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    user?.updateProfile(profileUpdates)?.await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                        Toast.makeText(this@DashboardActivity,"Successfully updated user profile",Toast.LENGTH_SHORT).show()
                    }


                }catch (e:Exception){
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DashboardActivity,e.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
    private fun checkLoggedInState(){
        val user = auth.currentUser
        if (user!=null){
            profile.setImageURI(user.photoUrl)
        }else{


        }
    }




}