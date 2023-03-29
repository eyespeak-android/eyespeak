package com.example.eyespeak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        val button: Button = findViewById(R.id.button3)
        button.setOnClickListener {
            val i = Intent(this@Profile, CameraTextbox::class.java)
            startActivity(i)
        }
    }
}