package com.magang.projectmaganglatihan.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityProfilBinding
import com.magang.projectmaganglatihan.model.MyProfileResponse
import com.magang.projectmaganglatihan.model.UpdateAvatarResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_profil.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ProfilActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfilBinding


    private lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)
//        imageUri = createImageUri()!!

        backPage()
        logout()
        editProfil()
        getDataProfil()
        changeAvatar()


    }


    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }


    private fun logout() {
        binding.tvLogout.setOnClickListener {
            sharedPref.clear()
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun editProfil() {
        val editProfil = findViewById<ImageView>(R.id.imgEditProfil)
        editProfil.setOnClickListener{
            val intent = Intent(this, EditProfilActivity::class.java)
            intent.putExtra("nikKaryawan", sharedPref.employeeNik)
            intent.putExtra("nama", sharedPref.employeeFullname)
            intent.putExtra("email", sharedPref.employeeEmail)
            intent.putExtra("jobDesk", sharedPref.departementTitle)
            intent.putExtra("noTelp", sharedPref.noTelp)
            startActivity(intent)
        }
    }


    private fun getDataProfil() {

        val parameter = HashMap<String, String>()
        parameter["employee_id"] = sharedPref.employeeId

        RetrofitClient.instance.getMyProfile(parameter, "Bearer ${sharedPref.tokenLogin}")
            .enqueue(object : Callback<MyProfileResponse> {
                override fun onResponse(call: Call<MyProfileResponse>, response: Response<MyProfileResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            val username : String = response.body()?.data!!.employeeFullname
                            val jobDesk : String = response.body()?.data!!.departement.departementTitle
                            val nip : String = response.body()?.data!!.employeeNik

                            tvUsername.text = username
                            tvJobDeskUser.text = jobDesk
                            tvNipUser.text = nip

                            SharedPrefManager.getInstance(this@ProfilActivity).saveCompanyId(response.body()?.data!!.companyId.toString())
                            SharedPrefManager.getInstance(this@ProfilActivity).saveEmployeeId(response.body()?.data!!.employeeId.toString())
                            SharedPrefManager.getInstance(this@ProfilActivity).saveEmployeeNik(response.body()?.data!!.employeeNik)
                            SharedPrefManager.getInstance(this@ProfilActivity).saveEmployeeFullname(response.body()?.data!!.employeeFullname)
                            SharedPrefManager.getInstance(this@ProfilActivity).saveEmployeeEmail(response.body()?.data!!.employeeEmail)
                            SharedPrefManager.getInstance(this@ProfilActivity).saveDepartementId(response.body()?.data!!.employeeDepartmentId.toString())
                            SharedPrefManager.getInstance(this@ProfilActivity).saveDepartementTitle(response.body()?.data!!.departement.departementTitle)
                            SharedPrefManager.getInstance(this@ProfilActivity).saveNoTelepon(response.body()?.data!!.profile.phoneNo.toString())


//                            myProfileAdapter = MyProfileAdapter(this@ProfilActivity, list)
//                            myProfileAdapter.notifyDataSetChanged()

                        } else {
                            Toast.makeText(this@ProfilActivity,response.body()!!.statusCode, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ProfilActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MyProfileResponse>, t: Throwable) {
                    Log.e("profil", "onFailure: " + t.message )
                    Toast.makeText(this@ProfilActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun changeAvatar() {
        binding.tvChangePhoto.setOnClickListener {
            ImagePicker.with(this@ProfilActivity)
                .crop() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                ) //Final image resolution will be less than 1080 x 1080(Optional)
//                .saveDir(File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "ImagePicker"))
                .start(10)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            val imageUri: Uri = data?.data!!
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(imageUri.toString()))
            val profil = findViewById<ImageView>(R.id.imgProfil)
            profil.setImageBitmap(bitmap)
            postAvatar(bitmap)

        }

    }



    private fun createTempFile(bitmap: Bitmap): File {
        val file = File(
            getExternalFilesDir(DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_image.webp"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, 0, bos)
        val bitmapData = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun postAvatar(gambarBitmap: Bitmap) {

        val file = createTempFile(gambarBitmap)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val employeeId: RequestBody =
            sharedPref.employeeId.toRequestBody("text/plain".toMediaTypeOrNull())
        val companyId: RequestBody =
            sharedPref.companyId.toRequestBody("text/plain".toMediaTypeOrNull())

        RetrofitClient.instance.postAvatar(
            "Bearer ${sharedPref.tokenLogin}",
            image,
            employeeId,
            companyId)
            .enqueue(object : Callback<UpdateAvatarResponse> {
                override fun onResponse(
                    call: Call<UpdateAvatarResponse>,
                    response: Response<UpdateAvatarResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            Toast.makeText(this@ProfilActivity,
                                "Foto Profil Berhasil Diubah",
                                Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(this@ProfilActivity,
                                response.body()!!.statusCode,
                                Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ProfilActivity,
                            "${response.body()?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateAvatarResponse>, t: Throwable) {
                    Log.e("avatar", "onFailure: " + t.message)
                    Toast.makeText(this@ProfilActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

}