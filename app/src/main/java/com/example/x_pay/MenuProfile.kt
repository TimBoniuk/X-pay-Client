package com.example.x_pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)
        val adminId = intent.getStringExtra("admin_id")
        Log.d("Menu", "admin_id: $adminId")

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://x-pay.biflow.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val service = retrofit.create(Login.LoginService::class.java)

        val CustomerLogOut = findViewById<RelativeLayout>(R.id.relativeLayout5)
        val passwordChange = findViewById<RelativeLayout>(R.id.relativeLayout3)
        passwordChange.setOnClickListener{
            val intent = Intent(this, ChangePassword::class.java)
            intent.putExtra("admin_id", adminId)
            startActivity(intent)
        }
        CustomerLogOut.setOnClickListener {
            val action = "logout".toRequestBody()
            val userId = adminId?.toRequestBody()
            val logoutFrom = "Admin".toRequestBody()

            if (userId != null) {
                service.logoutUser(
                    action,
                    userId,
                    logoutFrom,
                    "d6fb25b19015f65a5ad93b7663252c37"
                ).enqueue(object: Callback<Login.LogoutResponse> {
                    override fun onResponse(
                        call: Call<Login.LogoutResponse>,
                        response: Response<Login.LogoutResponse>
                    ) {
                        if (response.isSuccessful && response.body()?.result == "Success") {
                            Log.i("LogoutActivity", "Logged out successfully")
                            val intent = Intent(this@MenuProfile, Login::class.java)
                            startActivity(intent)
                        } else {
                            Log.e("LogoutActivity", "Failed to log out")
                            Toast.makeText(this@MenuProfile, "Failed to log out", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Login.LogoutResponse>, t: Throwable) {
                        Log.e("LogoutActivity", "Error: ${t.message}")
                        Toast.makeText(this@MenuProfile, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}