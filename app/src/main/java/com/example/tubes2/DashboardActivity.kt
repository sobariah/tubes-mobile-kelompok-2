package com.example.tubes2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
     auth= FirebaseAuth.getInstance();
        buttonregister.setOnClickListener {
            if (editEmail.text.trim().toString().isNotEmpty() || editPassword.text.trim().toString().isNotEmpty()){
                createUser(editEmail.text.trim().toString(), editPassword.text.trim().toString())
//            Toast.makeText(this,"iput provided",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"input required",Toast.LENGTH_LONG).show()
            }
        }
        tvLogin.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java);
            startActivity(intent)
        }
    }
    fun createUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful){
                    Log.e("Task Messege", "Successful...");
                        var intent= Intent(this,MainActivity::class.java);
                        startActivity(intent);
                    }else{
                        Log.e("Task Messege", "Failed..." +task.exception);
                    }

                }


    }
}