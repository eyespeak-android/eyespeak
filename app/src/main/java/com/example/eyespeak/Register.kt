package com.example.eyespeak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        val button: Button = findViewById(R.id.button2)
        button.setOnClickListener {
            val i = Intent(this@Register, CameraTextbox::class.java)
            startActivity(i)
        }
    }
}