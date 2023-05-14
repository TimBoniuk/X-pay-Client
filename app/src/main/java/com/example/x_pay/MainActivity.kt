package com.example.x_pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val adminId = intent.getStringExtra("admin_id")
        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        val adminEmail = intent.getStringExtra("admin_email")
        val password = intent.getStringExtra("password")
        val mobileNumber = intent.getStringExtra("mobile_number")
        val photoUrl = intent.getStringExtra("photo_url")
        val photoThumbUrl = intent.getStringExtra("photo_thumb_url")
        val verifyCode = intent.getStringExtra("verify_code")
        val deviceId = intent.getStringExtra("device_id")
        val loginStatus = intent.getStringExtra("login_status")
        val lastLogin = intent.getStringExtra("last_login")
        val onlineOfflineStatus = intent.getStringExtra("online_offline_status")
        val sessionId = intent.getStringExtra("session_id")
        val otpVerification = intent.getStringExtra("otp_verification")
        val customerSupportEmailAddress = intent.getStringExtra("customer_support_email_address")
        val addedDate = intent.getStringExtra("added_date")
        val customerSupportPhoneNumber = intent.getStringExtra("customer_support_phone_number")
        val deviceToken = intent.getStringExtra("device_token")
        val deviceType = intent.getStringExtra("device_type")
        Log.d("MainActivity", "admin_id: $adminId")
        Log.d("MainActivity", "name: $name")
        Log.d("MainActivity", "username: $username")

        val Customer = findViewById<LinearLayout>(R.id.Customer)
        val addPos = findViewById<LinearLayout>(R.id.addPos)
        Customer.setOnClickListener {
            val intent = Intent(this, MenuProfile::class.java)
            intent.putExtra("admin_id", adminId)
            startActivity(intent)
        }
        addPos.setOnClickListener {
            val intent = Intent(this, ListOfClients::class.java)
            startActivity(intent)
        }

    }
}