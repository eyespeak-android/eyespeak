package com.example.eyespeak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CameraTextbox : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_textbox)
        val button:Button = findViewById(R.id.button4)
        button.setOnClickListener {
            val intent = Intent(this@CameraTextbox, Profile::class.java)
            startActivity(intent)
        }
    }
}



