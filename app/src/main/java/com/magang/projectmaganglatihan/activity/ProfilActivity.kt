package com.magang.projectmaganglatihan.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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
    private var myProfileResponse : MyProfileResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)

        backPage()
        logout()
        editProfil()
        changeAvatar()

    }

    override fun onStart() {
        super.onStart()
        getDataProfil()

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
            intent.putExtra("NIK_KARYAWAN", myProfileResponse?.data!!.employeeNik)
            intent.putExtra("NAMA", myProfileResponse?.data!!.employeeFullname)
            intent.putExtra("EMAIL", myProfileResponse?.data!!.employeeEmail)
            intent.putExtra("JOB_DESK", myProfileResponse?.data!!.departement.departementTitle)
//            intent.putExtra("NO_TELP", myProfileResponse?.data!!.profile.phoneNo)
            startActivity(intent)
        }
    }


    private fun getDataProfil() {
        val parameter = HashMap<String, String>()
        parameter["employee_id"] = sharedPref.employeeId

        RetrofitClient.instance.getMyProfile(parameter, "Bearer ${sharedPref.tokenLogin}")
            .enqueue(object : Callback<MyProfileResponse> {
                override fun onResponse(
                    call: Call<MyProfileResponse>,
                    response: Response<MyProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            myProfileResponse = response.body()


                            val avatar : String = response.body()?.data!!.profile.employeeAvatar
                            val imageBytes = Base64.decode(avatar, Base64.DEFAULT)
                            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                            val username : String = response.body()?.data!!.employeeFullname
                            val jobDesk : String = response.body()?.data!!.departement.departementTitle
                            val nip : String = response.body()?.data!!.employeeNik


                            imgProfil.setImageBitmap(image)
//                            imgProfil.setImageBitmap(stringToBitmap(avatar))
                            tvUsername.text = username
                            tvJobDeskUser.text = jobDesk
                            tvNipUser.text = nip

//                            val avatar = findViewById<ImageView>(R.id.imgProfil)
//                            val image = response.body()?.data!!.employeeImage
//                            avatar.setImageBitmap(stringToBitmap(image))

//                                myProfileAdapter = MyProfileAdapter(this@HomeActivity, list)
//                                myProfileAdapter.notifyDataSetChanged()

                        } else {
                            Toast.makeText(
                                this@ProfilActivity,
                                response.body()!!.statusCode,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ProfilActivity,
                            "${response.body()?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<MyProfileResponse>, t: Throwable) {
                    Log.e("profil", "onFailure: " + t.message)
                    Toast.makeText(this@ProfilActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })

    }


    fun stringToBitmap(avatar: String): Bitmap? {
        val imageBytes = Base64.decode(avatar, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
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

//            Uri uri = data.getData();
//            byte[] imageBytes = getBytesfromURI(getApplicationContext(), uri);
//            String base64Encoded = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//            byte[] decodedString = Base64.decode(base64Encoded, Base64.DEFAULT)
//            Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            imgProfil.setImageBitmap(decodeByte)

//            val uri : Uri = data?.data!!
//            val imageBytes : ByteArray = get(applicationContext, uri);




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
