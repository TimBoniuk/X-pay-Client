package com.example.x_pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChangePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_change)

        val changepass= findViewById<TextView>(R.id.registerPass)
        val OldPassword= findViewById<EditText>(R.id.OldPassword)
        val NewPassword= findViewById<EditText>(R.id.NewPassword)
        val ConfirmPassword= findViewById<EditText>(R.id.ConfirmPassword)
        val adminId = intent.getStringExtra("admin_id")
        Log.d("Ch", "admin_id: $adminId")

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://x-pay.biflow.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val service = retrofit.create(Login.LoginService::class.java)

        changepass.setOnClickListener {
            val oldPasswordText = OldPassword.text.toString()
            val newPasswordText = NewPassword.text.toString()
            val confirmPasswordText = ConfirmPassword.text.toString()

            if (oldPasswordText.isEmpty() || newPasswordText.isEmpty() || confirmPasswordText.isEmpty()) {
                Toast.makeText(this, "Please fill in all the password fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPasswordText != confirmPasswordText) {
                Toast.makeText(this, "New password and confirmation password do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (oldPasswordText == newPasswordText) {
                Toast.makeText(this, "New password should be different from the old password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val action = "changePassword".toRequestBody()
            val userId = adminId?.toRequestBody()
            val changeFor = "Admin".toRequestBody()
            val oldPassword = oldPasswordText.toRequestBody()
            val newPassword = newPasswordText.toRequestBody()
            val confirmPassword = confirmPasswordText.toRequestBody()

            if (userId != null) {
                service.changePassword(
                    action,
                    userId,
                    changeFor,
                    oldPassword,
                    newPassword,
                    confirmPassword,
                    "d6fb25b19015f65a5ad93b7663252c37"
                ).enqueue(object : Callback<Login.ChangePasswordResponse> {
                    override fun onResponse(
                        call: Call<Login.ChangePasswordResponse>,
                        response: Response<Login.ChangePasswordResponse>
                    ) {
                        if (response.isSuccessful && response.body()?.result == "Success") {
                            Log.i("ChangePasswordActivity", "Password changed successfully")
                            Toast.makeText(this@ChangePassword, "Password changed successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ChangePassword, Login::class.java)
                            startActivity(intent)
                        } else {
                            Log.e("ChangePasswordActivity", "Failed to change password")
                            Log.e("ChangePasswordActivity", "Server response: ${response.errorBody()?.string()}")
                            Toast.makeText(this@ChangePassword, "Failed to change password", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Login.ChangePasswordResponse>, t: Throwable) {
                        Log.e("ChangePasswordActivity", "Error: ${t.message}")
                        Toast.makeText(this@ChangePassword, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}