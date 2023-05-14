package com.example.x_pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.x_pay.MainActivity
import com.example.x_pay.R
import okhttp3.Interceptor

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


class Login : AppCompatActivity() {

    interface LoginService {
        @Multipart
        @POST("index.php/apis")
        fun loginUser(
            @Part("action") action: RequestBody,
            @Part("email_address") email: RequestBody,
            @Part("device_token") deviceToken: RequestBody,
            @Part("password") password: RequestBody,
            @Part("device_type") deviceType: RequestBody,
            @Part("login_by") loginBy: RequestBody,
            @Query("token") token: String
        ): Call<LoginResponse>

        @Multipart
        @POST("index.php/apis")
        fun logoutUser(
            @Part("action") action: RequestBody,
            @Part("user_id") userId: RequestBody,
            @Part("logout_from") logoutFrom: RequestBody,
            @Query("token") token: String
        ): Call<LogoutResponse>
        @Multipart
        @POST("index.php/apis")
        fun resetPassword(
            @Part("action") action: RequestBody,
            @Part("email_address") email: RequestBody,
            @Part("reset_for") resetFor: RequestBody,
            @Query("token") token: String
        ): Call<ResetPasswordResponse>
        @Multipart
        @POST("index.php/apis")
        fun changePassword(
            @Part("action") action: RequestBody,
            @Part("user_id") userId: RequestBody,
            @Part("change_for") changeFor: RequestBody,
            @Part("old_password") oldPassword: RequestBody,
            @Part("new_password") newPassword: RequestBody,
            @Part("confirm_password") confirmPassword: RequestBody,
            @Query("token") token: String
        ): Call<ChangePasswordResponse>
    }




    data class LoginResponse(
        val result: String,
        val msg: String,
        val details: UserDetails
    )
    data class ChangePasswordResponse(
        val result: String,
        val msg: String,
        val details: UserDetails
    )
    data class LogoutResponse(
        val result: String,
        val msg: String
    )
    data class ResetPasswordResponse(
        val result: String,
        val msg: String
    )


    data class UserDetails(
        val admin_id: String,
        val name: String,
        val username: String,
        val admin_email: String,
        val password: String,
        val mobile_number: String,
        val photo_url: String,
        val photo_thumb_url: String,
        val verify_code: String,
        val device_id: String,
        val login_status: String,
        val last_login: String,
        val online_offline_status: String,
        val session_id: String,
        val otp_verification: String,
        val customer_support_email_address: String,
        val added_date: String,
        val customer_support_phone_number: String,
        val device_token: String,
        val device_type: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val btnLogin = findViewById<TextView>(R.id.loginbtn)
        val forgotpass = findViewById<TextView>(R.id.forgotpass)
        val email4login = findViewById<EditText>(R.id.email)
        val password4login = findViewById<EditText>(R.id.Password)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://x-pay.biflow.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()



        val service = retrofit.create(LoginService::class.java)

        forgotpass.setOnClickListener {
            val intent = Intent(this@Login, ForgotPassword::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val action = "Login".toRequestBody()
            val email = email4login.text.toString().toRequestBody()
            val deviceToken = "00000000000".toRequestBody()
            val password = password4login.text.toString().toRequestBody()
            val deviceType = "android".toRequestBody()
            val loginBy = "Admin".toRequestBody()

            service.loginUser(
                action,
                email,
                deviceToken,
                password,
                deviceType,
                loginBy,
                "d6fb25b19015f65a5ad93b7663252c37"
            ).enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    response.body()?.let { loginResponse ->
                        Log.i("LoginActivity", "Result: ${loginResponse.result}")
                        Log.i("LoginActivity", "Message: ${loginResponse.msg}")
                        Log.i("LoginActivity", "User Details: ${loginResponse.details}")

                        if (response.isSuccessful && loginResponse.result == "Success") {
                            Log.i("LoginActivity", "Login Successful")
                            val intent = Intent(this@Login, MainActivity::class.java)
                            val userDetails = loginResponse.details
                            intent.putExtra("admin_id", userDetails.admin_id)
                            intent.putExtra("name", userDetails.name)
                            intent.putExtra("username", userDetails.username)
                            intent.putExtra("admin_email", userDetails.admin_email)
                            intent.putExtra("password", userDetails.password)
                            intent.putExtra("mobile_number", userDetails.mobile_number)
                            intent.putExtra("photo_url", userDetails.photo_url)
                            intent.putExtra("photo_thumb_url", userDetails.photo_thumb_url)
                            intent.putExtra("verify_code", userDetails.verify_code)
                            intent.putExtra("device_id", userDetails.device_id)
                            intent.putExtra("login_status", userDetails.login_status)
                            intent.putExtra("last_login", userDetails.last_login)
                            intent.putExtra("online_offline_status", userDetails.online_offline_status)
                            intent.putExtra("session_id", userDetails.session_id)
                            intent.putExtra("otp_verification", userDetails.otp_verification)
                            intent.putExtra("customer_support_email_address", userDetails.customer_support_email_address)
                            intent.putExtra("added_date", userDetails.added_date)
                            intent.putExtra("customer_support_phone_number", userDetails.customer_support_phone_number)
                            intent.putExtra("device_token", userDetails.device_token)
                            intent.putExtra("device_type", userDetails.device_type)
                            startActivity(intent)
                        } else if (loginResponse.result == "Failed" && loginResponse.msg == "Wrong email or password") {
                            Toast.makeText(this@Login, "Wrong email or password", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@Login, "Error occurred", Toast.LENGTH_LONG).show()
                        }
                    } ?: run {
                        response.errorBody()?.let {
                            //Log.e("LoginActivity", "Error Body: ${it.string()}")
                        }
                        Toast.makeText(this@Login, "Login failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@Login, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("token", "d6fb25b19015f65a5ad93b7663252c37")
            .build()
        return chain.proceed(newRequest)
    }
}




