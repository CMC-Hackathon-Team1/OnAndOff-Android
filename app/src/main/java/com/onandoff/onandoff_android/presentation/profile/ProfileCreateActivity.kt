package com.onandoff.onandoff_android.presentation.profile

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
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
import com.onandoff.onandoff_android.databinding.DialogProfileCreateAlertBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.Camera.FLAG_PERM_STORAGE
import com.onandoff.onandoff_android.util.Camera.STORAGE_PERMISSION
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.create
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class ProfileCreateActivity:AppCompatActivity() {
    private lateinit var binding: ActivityProfileCreateBinding

    //권한 가져오기
    var imgFile: MultipartBody.Part? = null
    var isValid = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivArrow.setOnClickListener {
            finish()
        }
        binding.ivProfileBackground.setOnClickListener {
            showBottomSheet(context = this)

        }
        binding.tvFinish.setOnClickListener {
            //dialog 띄워주기
            val img: File
            val userId = prefs.getSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_USERID, 0)
            if (binding.etPersonas.length() == 0) {
                binding.viewPersonas.setBackgroundColor(this.getColor(R.color.errorColor))
                binding.tvPersonasError.setTextColor(this.getColor(R.color.errorColor))

            } else if (binding.etNickname.length() == 0) {
                binding.viewNickname.setBackgroundColor(this.getColor(R.color.errorColor))
                binding.tvPersonasError.setTextColor(this.getColor(R.color.errorColor))
            } else {
                checkDialog()
            }

        }

    }
    //프로필 생성 확인 alert 창
    fun checkDialog(){
        val dialog = Dialog(this@ProfileCreateActivity)
        val dialogView = DialogProfileCreateAlertBinding.inflate(LayoutInflater.from(this@ProfileCreateActivity))
        dialog.setContentView(dialogView.root)
        val params : WindowManager.LayoutParams? = dialog.window?.attributes;
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        if (params != null) {
            dialog.window?.setLayout(params.width,params.height)
        }
        dialog.show()
        dialogView.btnYes.setOnClickListener{
            Log.d("profile create","동의 완료")
            isValid = true
            val nickname = binding.etNickname.text.toString()
            val personas = binding.etPersonas.text.toString()
            val statusmsg = binding.etOneline.text.toString()
            val call = createProfile(nickname, personas, statusmsg,null)
            call?.let { it1 -> getData(it1) }
//
        }
        dialogView.btnNo.setOnClickListener{
            dialog.dismiss()
        }
    }

    //갤러리, 기본이미지 변경여부 체크
    fun showBottomSheet(context: Context){

        val dialog = BottomSheetDialog(context)
        val dialogView = BottomsheetSelectProfileImageBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogView.root)
        dialog.show()
        dialogView.layoutGallery.setOnClickListener{
            if(checkPermission(STORAGE_PERMISSION,FLAG_PERM_STORAGE) ){
                move_gallery()
            }
            dialog.dismiss()
        }
        dialogView.layoutBasic.setOnClickListener{
            imgFile = null
            binding.ivProfileAvatar.setImageResource(R.drawable.icon_profile_default)
            binding.ivProfileBackground.visibility = View.VISIBLE
            Log.d("profile",imgFile.toString())
            Log.d("profile",binding.ivProfileBackground.visibility.toString())
            dialog.dismiss()
        }

    }
    //api에 보내기전 Multipart로 convert하는 함수
    fun createProfile(profileName:String, personaName:String,statusMessage:String,img:Uri?): Call<ProfileResponse>? {
        val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(ProfileInterface::class.java)
        val formProfileName = FormDataUtil.getBody("profileName", profileName)       // 2-way binding 되어 있는 LiveData
        val formPersonaName = FormDataUtil.getBody("personaName", personaName)    // 2-way binding 되어 있는 LiveData
        val formStatusMsg = FormDataUtil.getBody("statusMessage", statusMessage)    // 2-way binding 되어 있는 LiveData
        val formImg:MultipartBody.Part
        val call:Call<ProfileResponse>?
        Log.d("gallery","$imgFile")
        call = imgFile?.let {
            profileInterface?.profileCreate(formProfileName,formPersonaName,formStatusMsg,
                it
            )
        }?: profileInterface?.profileCreate(formProfileName,formPersonaName,formStatusMsg)

        return call
    }
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    fun move_gallery() {
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(photoPickerIntent, FLAG_PERM_STORAGE)
    }


    //checkPermission() 에서 ActivityCompat.requestPermissions 을 호출한 다음 사용자가 권한 허용여부를 선택하면 해당 메소드로 값이 전달 됩니다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            FLAG_PERM_STORAGE -> {
                Log.d("permission","2")
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인하지않으면 앱을 실행할수없습니다", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        return
                    }
                }
                move_gallery()
            }

        }
    }

    var realUri:Uri?=null
    fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        Log.d("permission", "실행됨?")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("permission", "1")
            for (permission in permissions) {
                //만약 권한이 승인되어 있지 않다면 권한승인 요청을 사용에 화면에 호출합니다.
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(this, permissions, flag)
                    return false
                }
            }
        }
        return true
    }
    private fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null) ?: return uri.path ?: ""
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(columnIndex)
        cursor.close()
        return path
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("gallery","req=$requestCode, result = $resultCode, data =$data")
        if(resultCode== Activity.RESULT_OK){
            when(requestCode){
                //Gallery- 저장소 권한 Flag일때
                FLAG_PERM_STORAGE ->{
                    val uri = data?.data // 선택한 이미지의 Uri 객체
                    binding.ivProfileBackground.setImageURI(uri)
                    binding.ivAddAvatar.visibility = View.GONE
                    binding.ivAddBackground.visibility = View.GONE
                    binding.ivProfileAvatar.visibility = View.GONE
                    val filePath = uri?.let { getPathFromUri(it) }
                    val file = File(filePath)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                    imgFile = body

                    Log.d("gallery","${imgFile}")

                    }

                }

            }
        }


    //api 받아오는 함수
    fun getData(call:Call<ProfileResponse>){
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
                            "${response.code()}"
                        );
                        Log.d(
                            "Profile Create",
                            response.body()?.result?.profileId!!.toString()
                        );

                        prefs.putSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,
                            response.body()?.result?.profileId!!.toString())

                        Toast.makeText(this@ProfileCreateActivity,"프로필 생성 성공! 메인화면으로 이동합니다", Toast.LENGTH_SHORT).show()
                        val Intent = Intent(this@ProfileCreateActivity, MainActivity::class.java)
                        startActivity(Intent)
                        finish()
                    }

                }

            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable){
                Toast.makeText(this@ProfileCreateActivity,"생성이 불가능합니다. 다시시도해주세요 ${t.toString()}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
