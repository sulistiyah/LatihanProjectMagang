package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityChangePasswordBinding
import com.magang.projectmaganglatihan.model.ChangePasswordResponse
import com.magang.projectmaganglatihan.model.EditProfilResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    private lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)

        backPage()
        saveChangePassword()

    }

    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveChangePassword() {
        binding.btnSimpan.setOnClickListener {
            val currentPass = binding.etPass.text.toString()
            val newPass = binding.etNewPass.text.toString()
            val newPassConfirm = binding.etNewPassConfirm.text.toString()


            val employeeId: RequestBody = sharedPref.employeeId.toRequestBody("text/plain".toMediaTypeOrNull())
            val currentPassword: RequestBody = currentPass.toRequestBody("text/plain".toMediaTypeOrNull())
            val newPassword : RequestBody = newPass.toRequestBody("text/plain".toMediaTypeOrNull())
            val newPasswordConfirm : RequestBody = newPassConfirm.toRequestBody("text/plain".toMediaTypeOrNull())

            RetrofitClient.instance.postChangePassword(
                "Bearer ${sharedPref.tokenLogin}",
                employeeId,
                currentPassword,
                newPassword,
                newPasswordConfirm).enqueue(object : Callback<ChangePasswordResponse> {
                override fun onResponse(
                    call: Call<ChangePasswordResponse>,
                    response: Response<ChangePasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

//                            val intent = Intent(this@ChangePasswordActivity, SplashActivity::class.java)
//                            startActivity(intent)


                            Toast.makeText(this@ChangePasswordActivity, "Berhasil", Toast.LENGTH_SHORT).show()

                            SharedPrefManager.getInstance(this@ChangePasswordActivity).saveChangePass(true)


                        } else {
                            Toast.makeText(this@ChangePasswordActivity,response.body()!!.statusCode, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ChangePasswordActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                    Log.e("ChangePassword", "onFailure: " + t.message )
                    Toast.makeText(this@ChangePasswordActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

}