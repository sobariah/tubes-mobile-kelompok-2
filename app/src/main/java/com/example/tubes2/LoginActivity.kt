package com.example.tubes2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth=FirebaseAuth.getInstance();

        tvRegister.setOnClickListener {
            val intent= Intent(this,DashboardActivity::class.java);
            startActivity(intent)
        }
        btnlogin.setOnClickListener {
            if (editTextEmail.text.trim().toString().isNotEmpty() || editTextPassword.text.trim().toString().isNotEmpty()  ){
                signInUser(editTextEmail.text.trim().toString(), editTextPassword.text.trim().toString() );
            }else{
                Toast.makeText(this,"input required",Toast.LENGTH_LONG).show();
            }
        }
    }
    fun signInUser(email:String, password:String){

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener (this){task ->
                    if (task.isSuccessful){
                    val intent=Intent(this,MainActivity::class.java);
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"error !!"+task.exception,Toast.LENGTH_LONG).show()
                    }
                }
    }
}