package com.onandoff.onandoff_android.presentation.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.FormDataUtil
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.ProfileResponse
import com.onandoff.onandoff_android.databinding.ActivityProfileCreateBinding
import com.onandoff.onandoff_android.databinding.BottomsheetSelectProfileImageBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.Camera.PERM_STORAGE
import com.onandoff.onandoff_android.util.Camera.STORAGE_PERMISSION
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val imageUrl = it.data?.data
            binding.ivProfileAvatar.setImageURI(imageUrl)
        }
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
        binding.ivProfileBackground.setOnClickListener{
            showBottomSheet(context = this)
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
                binding.tvPersonasError.setTextColor(this.getColor(R.color.errorColor))

            }else if(binding.etNickname.length() ==0){
                    binding.viewNickname.setBackgroundColor(this.getColor(R.color.errorColor))
                    binding.tvPersonasError.setTextColor(this.getColor(R.color.errorColor))
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
                                Toast.makeText(this@ProfileCreateActivity,"프로필은 3개까지 생성 가능합니다", Toast.LENGTH_LONG).show()
                            }
                            else->{
                                Log.d(
                                    "Profile Create",
                                    "retrofit manager called, onSucess called!"
                                );


                                prefs.putSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,
                                    mutableSetOf<String>(response.body()?.result?.profileId!!.toString())
                                );
                                Toast.makeText(this@ProfileCreateActivity,"프로필 생성 성공! 메인화면으로 이동합니다", Toast.LENGTH_SHORT).show()
                                val Intent = Intent(this@ProfileCreateActivity, MainActivity::class.java)
                                startActivity(Intent)
                                finish()
                            }

                        }

                    }
                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable){

                    }
                })
            }
        }

    }
    fun showBottomSheet(context: Context){

        val dialog = BottomSheetDialog(context)
        val dialogView = BottomsheetSelectProfileImageBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(dialogView.root)
        checkPermission(STORAGE_PERMISSION, PERM_STORAGE)
        dialogView.layoutCamera.setOnClickListener{
            move_camera()
        }
        dialogView.layoutGallery.setOnClickListener{
            move_gallery()
        }
        dialog.show()

    }

    fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("permission","1")
            for (permission in permissions) {
                //만약 권한이 승인되어 있지 않다면 권한승인 요청을 사용에 화면에 호출합니다.
                if (ContextCompat.checkSelfPermission(
                        this@ProfileCreateActivity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("permission","isGrandted?")
                    ActivityCompat.requestPermissions(this, permissions, flag)
                    Log.d("permission","isGrandted?")
                    return false
                }
            }
        }
        Log.d("permission","1.5")
return true
    }
    //checkPermission() 에서 ActivityCompat.requestPermissions 을 호출한 다음 사용자가 권한 허용여부를 선택하면 해당 메소드로 값이 전달 됩니다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERM_STORAGE -> {
                Log.d("permission","2")
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인하지않으면 앱을 실행할수없습니다", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        return
                    }
                }

            }

        }
    }
    fun move_gallery() {
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startForResult.launch(photoPickerIntent)
    }
    fun move_camera() {
        var cameraPickerIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraPickerIntent.type = "image/*"

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