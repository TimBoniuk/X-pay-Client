package com.example.x_pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotpassword)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://x-pay.biflow.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val service = retrofit.create(Login.LoginService::class.java)

        val submitbtn= findViewById<TextView>(R.id.submitbtn)
        val emailField= findViewById<EditText>(R.id.email)

        submitbtn.setOnClickListener {
            val emailText = emailField.text.toString()
            if (emailText.isEmpty()) {
                Toast.makeText(this, "Please fill in the email field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val action = "resetPassword".toRequestBody()
            val email = emailField.text.toString().toRequestBody()
            val resetFor = "Admin".toRequestBody()

            service.resetPassword(
                action,
                email,
                resetFor,
                "d6fb25b19015f65a5ad93b7663252c37"
            ).enqueue(object: Callback<Login.ResetPasswordResponse> {
                override fun onResponse(
                    call: Call<Login.ResetPasswordResponse>,
                    response: Response<Login.ResetPasswordResponse>
                ) {
                    if (response.isSuccessful && response.body()?.result == "Success") {
                        Log.i("ForgotPasswordActivity", "Password reset successful")
                        Toast.makeText(this@ForgotPassword, "Password sent to mail, please check", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@ForgotPassword, Login::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("ForgotPasswordActivity", "Failed to reset password")
                        Toast.makeText(this@ForgotPassword, "Failed to reset password", Toast.LENGTH_LONG).show()
                        Log.e("ForgotPasswordActivity", "Server response: ${response.body().toString()}")
                    }
                }

                override fun onFailure(call: Call<Login.ResetPasswordResponse>, t: Throwable) {
                    Log.e("ForgotPasswordActivity", "Error: ${t.message}")
                    Log.e("ForgotPasswordActivity", "Error: ${email}")
                    Log.e("ForgotPasswordActivity", "Error: ${resetFor}")
                    Log.e("ForgotPasswordActivity", "Error: ${action}")
                    Toast.makeText(this@ForgotPassword, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}