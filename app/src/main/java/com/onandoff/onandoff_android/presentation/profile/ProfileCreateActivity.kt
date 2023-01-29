package com.onandoff.onandoff_android.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.BuildConfig
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.FormDataUtil
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.ProfileRequest
import com.onandoff.onandoff_android.data.model.ProfileResponse
import com.onandoff.onandoff_android.databinding.ActivityProfileCreateBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.Camera
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ProfileCreateActivity:AppCompatActivity() {
    private lateinit var binding: ActivityProfileCreateBinding
    //권한 가져오기
    lateinit var imgFile:File
    val isValid = false
    private val readImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Glide.with(this)
            .load(uri)
            .into(binding.ivProfileAvatar)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        if (checkPermission(Camera.STORAGE_PERMISSION, Camera.PERM_STORAGE)) {
//            setViews()
//        }
        binding.ivArrow.setOnClickListener {
            finish()
        }
        binding.ivProfileAvatar.setOnClickListener{
            readImage.launch("image/*")
        }

        binding.tvFinish.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val personas = binding.etPersonas.text.toString()
            val statusmsg =binding.etOneline.text.toString()
            val img:File
            val userId = prefs.getSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_USERID,0)
            if(binding.etPersonas.length() ==0){
                binding.viewPersonas.setBackgroundColor(this.getColor(R.color.errorColor))
                binding.tvPersonasError.setBackgroundColor(this.getColor(R.color.errorColor))

            }else if(binding.etNickname.length() ==0){
                    binding.viewNickname.setBackgroundColor(this.getColor(R.color.errorColor))
                    binding.tvPersonasError.setBackgroundColor(this.getColor(R.color.errorColor))
            }
            else{
//                val call = createProfile(userId,nickname,personas,img,statusmsg)
                val call = createProfile(userId,nickname,personas,statusmsg)
                call?.enqueue(object: Callback<ProfileResponse>{
                    override fun onResponse(
                        call: Call<ProfileResponse>,
                        response: Response<ProfileResponse>
                    ){
                        when(response.body()?.statusCode){
                            1500->{
                                Log.d(
                                    "Profile Create",
                                    "retrofit manager called, onSucess called but already join!"
                                );
                            }
                            else->{
                                Log.d(
                                    "Profile Create",
                                    "retrofit manager called, onSucess called!"
                                );
                                prefs.putSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,
                                    response.body()?.result?.profileId!!
                                );
                                Toast.makeText(this@ProfileCreateActivity,"로그인성공! 프로필 생성해주세요:)", Toast.LENGTH_SHORT).show()
                                val Intent = Intent(this@ProfileCreateActivity, MainActivity::class.java)
                                startActivity(Intent)
                            }

                        }

                    }
                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable){

                    }
                })
            }
        }

    }
    fun createProfile(userId: Int, profileName:String, personaName:String,statusMessage:String ): Call<ProfileResponse>? {
        val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(ProfileInterface::class.java)
        val formId = FormDataUtil.getBody("userId", userId)
        val formProfileName = FormDataUtil.getBody("profileName", profileName)       // 2-way binding 되어 있는 LiveData
        val formPersonaName = FormDataUtil.getBody("personaName", personaName)    // 2-way binding 되어 있는 LiveData
        val formStatusMsg = FormDataUtil.getBody("statusMessage", statusMessage)    // 2-way binding 되어 있는 LiveData
//        val formImg = FormDataUtil.getImageBody("media", img)
        val call = profileInterface?.profileCreate(formId,formProfileName,formPersonaName,formStatusMsg)
        return call
    }
//    private fun selectCamera() {
//        var permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//        if (permission == PackageManager.PERMISSION_DENIED) {
//            // 권한 없어서 요청
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), Camera.FLAG_REQ_CAMERA)
//        } else {
//            // 권한 있음
//            var state = Environment.getExternalStorageState()
//            if (TextUtils.equals(state, Environment.MEDIA_MOUNTED)) {
//                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                intent.resolveActivity(packageManager)?.let {
//
//                    var photoFile: File? = createImageFile()
//                    photoFile?.let {
//                        var photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", it)
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//                        startActivityForResult(intent, REQ_IMAGE_CAPTURE)
//                    }
//                }
//            }
//        }
//    }
//    private fun createImageFile(): File {
//        // 사진이 저장될 폴더 있는지 체크
//        var file = File(Environment.getExternalStorageDirectory(), "/path/")
//        if (!file.exists()) file.mkdir()
//
//        var imageName = "fileName.jpeg"
//        var imageFile = File("${Environment.getExternalStorageDirectory().absoluteFile}/path/", "$imageName")
//        imagePath = imageFile.absolutePath
//        return imageFile
//    }

}