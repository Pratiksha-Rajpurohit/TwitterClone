package com.example.twitterclone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class ProfileActivity : AppCompatActivity() {
    private lateinit var profileImage : CircleImageView
    private lateinit var openGallery : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        openGallery.setOnClickListener {

            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 101)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == RESULT_OK) {

            profileImage.setImageURI(data?.data)
            uploadProfileImage(data?.data)

        }
    }

    private fun uploadProfileImage(uri : Uri?) {

        val profileImageName = UUID.randomUUID().toString()+".jpg"
        val storageReference = FirebaseStorage.getInstance().getReference().child("profile_Images/$profileImageName")

        storageReference.putFile(uri!!).addOnSuccessListener {

            val result = it.metadata?.reference?.downloadUrl

            result?.addOnSuccessListener {

                FirebaseDatabase.getInstance().reference.child("users").child(Firebase.auth.uid!!.toString()).child("userProfileImage").setValue(it.toString())

            }
        }
    }

    private fun init() {
        profileImage = findViewById(R.id.profile_image)
        openGallery = findViewById(R.id.open_gallery)

        FirebaseDatabase.getInstance().reference.child("users").child(Firebase.auth.uid!!.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val link = snapshot.child("userProfileImage").value.toString()


                    if(!link.isNullOrBlank()){
                        Glide.with(this@ProfileActivity)
                            .load(link)
                            .into(profileImage)

                    }else{

                        println("OOOOOOOOOOOOOppppppppppppppppppppppppssssssssssssss it's not working :(  ${snapshot.child("userProfileImage").value.toString()}f")

                        profileImage.setImageResource(R.drawable.ic_launcher_background)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


//        FirebaseDatabase.getInstance().reference.child("users").child(Firebase.auth.uid!!.toString()).child("userProfileImage")
//            .get().addOnSuccessListener {
//                if (it.value != null) {
////                    profileImage.setImageURI(Uri.parse(it.value.toString()))
//                    Glide.with(this).load(it.value.toString()).into(profileImage)
//                }
//            }

    }
}