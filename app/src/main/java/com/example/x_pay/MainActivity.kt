package com.example.x_pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val Customer = findViewById<LinearLayout>(R.id.Customer)
        val addPos = findViewById<LinearLayout>(R.id.addPos)
        Customer.setOnClickListener {
            val intent = Intent(this, MenuProfile::class.java)
            startActivity(intent)
        }
        addPos.setOnClickListener {
            val intent = Intent(this, ListOfClients::class.java)
            startActivity(intent)
        }

    }
}