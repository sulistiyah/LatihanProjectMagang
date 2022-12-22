package com.magang.projectmaganglatihan.activity


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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
import java.io.File


class ProfilActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfilBinding


    private lateinit var sharedPref: SharedPrefManager
    private lateinit var imageUri : Uri
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2
    var filePath : String = " "
    private lateinit var file : File


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

                            tvUsername.setText(username)
                            tvJobDeskUser.setText(jobDesk)
                            tvNipUser.setText(nip)

                            SharedPrefManager.getInstance(this@ProfilActivity).saveEmployeeFullname(response.body()?.data!!.employeeFullname)

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

    fun onClickTvChangePhoto(view: View) {

        openDialog()

    }

    private fun openDialog() {
        val openDialog = AlertDialog.Builder(this)
        openDialog.setTitle("Pilih Foto Profil...")
        openDialog.setPositiveButton("Ambil Dari Kamera") {
                dialog,_->
            cameraPermission()
            openCamera()
            dialog.dismiss()

        }
        openDialog.setNegativeButton("Ambil dari Galeri") {
                dialog,_->
            galleryPermission()
            openGallery()
            postAvatar()
            dialog.dismiss()

        }
        openDialog.setNeutralButton("Cancel") {
                dialog,_->
            dialog.dismiss()

        }
        openDialog.create()
        openDialog.show()
    }


    private fun openCamera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)


    }

    private fun openGallery() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val imageTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
            it.putExtra(Intent.EXTRA_MIME_TYPES, imageTypes)
            startActivityForResult(it, GALLERY_REQUEST_CODE)

        }

    }

    private fun cameraPermission() {
        Dexter.withContext(this).withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    p0?.let {
                        if (p0.areAllPermissionsGranted()) {
                            openCamera()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?,
                ) {
                    openDialog()
                }
            })
    }

    private fun galleryPermission() {
        Dexter.withContext(this).withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    openGallery()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?,
                ) {
                    openDialog()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                CAMERA_REQUEST_CODE -> {
                    binding.imgProfil.setImageBitmap(data?.extras!!.get("data") as Bitmap)
//                    val bitmap = data?.extras!!.get("data") as Bitmap
//                    binding.imgProfil.load(bitmap) {
//                        crossfade(true)
//                        crossfade(1000)
//                        transformations(CircleCropTransformation())
//                    }
                }

                GALLERY_REQUEST_CODE -> {
                    imageUri = data?.data!!
                    binding.imgProfil.setImageURI(imageUri)
                }
            }
        }

    }

    private fun createImageUri() : Uri? {
        val image = File(applicationContext.filesDir, "image.photo.png")
        return FileProvider.getUriForFile(
            applicationContext,
            "com.magang.projectmaganglatihan.fileProvider",
            image
        )
    }


    private fun postAvatar() {

//        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//        val file = File(path, "DemoPicture.png")

//        val file : File = File(filePath)
//        val filesDir = applicationContext.filesDir
//        val file = File(filesDir, "image.photo.png")


//        val fileUri = Uri.parse(surveyResponse.imageUri)
//        String path = getPath(context, uri);
//        if (path != null && isLocal(path)) {
//            return new File(path);
//        }
//        val file = LocalStorageProvider.getFile(activity, fileUri)

        file = File(
            Environment.getExternalStorageDirectory(),
            "file" + System.currentTimeMillis().toString() + ".jpg"
        )


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