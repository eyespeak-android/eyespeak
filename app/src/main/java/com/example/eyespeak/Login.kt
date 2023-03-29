package com.example.eyespeak
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val i = Intent(this@Login, CameraTextbox::class.java)
            startActivity(i)
        }
    }
}