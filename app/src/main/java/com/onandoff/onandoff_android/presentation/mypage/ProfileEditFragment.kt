package com.onandoff.onandoff_android.presentation.mypage

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkPermission
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.FormDataUtil
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.ext.setImageUrl
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.databinding.BottomsheetSelectProfileImageBinding
import com.onandoff.onandoff_android.databinding.DialogProfileDeleteBinding
import com.onandoff.onandoff_android.databinding.DialogProfileDeleteDefaultBinding
import com.onandoff.onandoff_android.databinding.FragmentProfileEditBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.HomeFragment
import com.onandoff.onandoff_android.presentation.usercheck.SignInActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID
import com.onandoff.onandoff_android.util.Camera
import com.onandoff.onandoff_android.util.SharePreference
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileEditFragment: Fragment() {
    //권한 가져오기
    var imgFile: MultipartBody.Part? = null
    var isBasicImage:Boolean = false
    val TAG:String = "MYPAGE"
    var profileCount:Int = 0
    private lateinit var binding : FragmentProfileEditBinding
    val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(
        ProfileInterface::class.java)
    val profileId = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,0)
    var profileIdList = ArrayList<Int>()
    lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMyProfile()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var personaName = arguments?.getString("personaName")
        var nickName = arguments?.getString("nickName")
        var profileImg = arguments?.getString("profileImg")
        var statusMsg = arguments?.getString("statusMsg")
        Log.d("profilEdit","$personaName $nickName $profileImg $statusMsg")
        binding = FragmentProfileEditBinding.inflate(layoutInflater)
        binding.profile = ProfileEditData(personaName = personaName!!, profileName = nickName!!, profileImgUrl = profileImg!!, statusMessage = statusMsg!!)
//        binding.etPersonas.text = personaName.toString()
//        binding.tvNickname.text = nickName.toString()
//        binding.ivProfileAvatar.setImageUrl(profileImg.toString())
//        binding.tvOneline.text = statusMsg.toString()
        binding.ivArrow.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this@ProfileEditFragment).commit()
        }
        binding.ivProfileAvatar.setOnClickListener{
            showBottomSheet(mainActivity)
        }
        binding.tvMypageDelete.setOnClickListener{
            if (profileCount <=1){
                showDialog()
            }else{
                showDefaultDialog()
            }
        }
        binding.btSingup.setOnClickListener{
            editPersona()
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    //갤러리, 기본이미지 변경여부 체크
    fun showBottomSheet(context: Context){

        val dialog = BottomSheetDialog(context)
        val dialogView = BottomsheetSelectProfileImageBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogView.root)
        dialog.show()
        dialogView.layoutGallery.setOnClickListener{
            if(checkPermission(Camera.STORAGE_PERMISSION, Camera.FLAG_PERM_STORAGE) ){
                move_gallery()
            }
            isBasicImage = false
            dialog.dismiss()
        }
        dialogView.layoutBasic.setOnClickListener{
            imgFile = null
            binding.ivProfileAvatar.setImageResource(R.drawable.default_image)
            isBasicImage = true
            dialog.dismiss()
        }

    }
    fun move_gallery() {
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(photoPickerIntent, Camera.FLAG_PERM_STORAGE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("gallery","req=$requestCode, result = $resultCode, data =$data")
        if(resultCode== Activity.RESULT_OK){
            when(requestCode){
                //Gallery- 저장소 권한 Flag일때
                Camera.FLAG_PERM_STORAGE ->{
                    val uri = data?.data // 선택한 이미지의 Uri 객체
                    binding.ivProfileAvatar.setImageURI(uri)
                    val filePath = uri?.let { getPathFromUri(it,mainActivity) }
                    val file = File(filePath)
                    val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                    imgFile = body

                    Log.d("gallery","${imgFile}")

                }

            }

        }
    }
    private fun getPathFromUri(uri: Uri, context: Context): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null) ?: return uri.path ?: ""
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(columnIndex)
        cursor.close()
        return path
    }

    fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        Log.d("permission", "실행됨?")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("permission", "1")
            for (permission in permissions) {
                //만약 권한이 승인되어 있지 않다면 권한승인 요청을 사용에 화면에 호출합니다.
                if (ContextCompat.checkSelfPermission(
                        mainActivity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(mainActivity, permissions, flag)
                    return false
                }
            }
        }
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
            Camera.FLAG_PERM_STORAGE -> {
                Log.d("permission","2")
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(mainActivity, "저장소 권한을 승인하지않으면 앱을 실행할수없습니다", Toast.LENGTH_SHORT)
                            .show()
//                        finish()
                        return
                    }
                }
                move_gallery()
            }

        }
    }

    fun getMyProfile() {
        val call = profileInterface?.profileCheck()
        call?.enqueue(object: Callback<ProfileListResponse> {
            override fun onResponse(
                call: Call<ProfileListResponse>,
                response: Response<ProfileListResponse>
            ){
                Log.d(TAG,"${response.code()}")
                profileCount = response.body()?.result?.size ?: 0

                for (i in 0 until profileCount ){
                    response.body()?.result?.get(i)?.profileId?.let { profileIdList.add(it) }
                }
                for(index in profileIdList.indices) println("fruits[$index] : ${profileIdList[index]}")
            }
            override fun onFailure(call: Call<ProfileListResponse>, t: Throwable){

            }
        })
    }
    fun editPersona(){
        var etProfileName = binding.etNickname.text
        var etStatusMsg = binding.etOneline.text
        // TODO : call lateinit 시 초기홥아법
        lateinit var call :Call<ProfileListResultResponse>
        if(imgFile !=null) {
            call = profileInterface?.profileEdit(profileId,
                FormDataUtil.getBody("profileName", etProfileName.toString()) , FormDataUtil.getBody("statusMessage", etStatusMsg.toString()),image= imgFile,defaultImage= FormDataUtil.getBody("defaultImage", isBasicImage))!!
        }else{
            call = profileInterface?.profileEdit(profileId,
                FormDataUtil.getBody("profileName", etProfileName.toString()) , FormDataUtil.getBody("statusMessage", etStatusMsg.toString()),defaultImage= FormDataUtil.getBody("defaultImage", isBasicImage))!!
        }
        call?.enqueue(object: Callback<ProfileListResultResponse> {
            override fun onResponse(
                call: Call<ProfileListResultResponse>,
                response: Response<ProfileListResultResponse>
            ) {
                Toast.makeText(mainActivity,"프로필 편집이 성공했습니다",Toast.LENGTH_SHORT).show()
                mainActivity.supportFragmentManager.beginTransaction()
                    .remove(this@ProfileEditFragment).commit()

            }
            override fun onFailure(call: Call<ProfileListResultResponse>, t: Throwable){
                Toast.makeText(mainActivity,"프로필 편집이 실패했습니다${t}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deletePersona(){
        val call = profileInterface?.profileDelete(profileId)
        call?.enqueue(object: Callback<ProfileResult> {
            override fun onResponse(
                call: Call<ProfileResult>,
                response: Response<ProfileResult>
            ){
                if(response.code() ==1504){
                    Toast.makeText(mainActivity,"이미 해당 프로필이 삭제되었습니다!",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(mainActivity,"프로필을 성공적으로 삭제했습니다!",Toast.LENGTH_SHORT).show()
                    getMyProfile()
                    prefs.putSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,profileIdList[0])
                }

            }
            override fun onFailure(call: Call<ProfileResult>, t: Throwable){
                Toast.makeText(mainActivity,"프로필 삭제가 실패했습니다${t}",Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun showDefaultDialog(){
        val dialog = Dialog(mainActivity)
        val dialogView = DialogProfileDeleteDefaultBinding.inflate(LayoutInflater.from(mainActivity))
        dialog.setContentView(dialogView.root)
        val params : WindowManager.LayoutParams? = dialog.window?.attributes;
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        if (params != null) {
            dialog.window?.setLayout(params.width,params.height)
        }
        dialog.show()
        dialogView.btnNo.setOnClickListener{
            dialog.dismiss()
        }
        dialogView.btnYes.setOnClickListener{
            deletePersona()
            // TODO : sharedPreference에 저장된 현재 profileId 바꿔주기
//            prefs.putSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID, profileId!!)
            val homeFragment = HomeFragment()
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main,homeFragment)
                .commit()
            dialog.dismiss()
        }
    }
    fun showDialog(){
        val dialog = Dialog(mainActivity)
        val dialogView = DialogProfileDeleteBinding.inflate(LayoutInflater.from(mainActivity))
        dialog.setContentView(dialogView.root)
        val params : WindowManager.LayoutParams? = dialog.window?.attributes;
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        if (params != null) {
            dialog.window?.setLayout(params.width,params.height)
        }
        dialog.show()
        dialogView.btnYes.setOnClickListener{
            val homeFragment = HomeFragment()
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main,homeFragment)
                .commit()
            dialog.dismiss()
        }
        dialogView.btnNo.setOnClickListener{
            dialog.dismiss()
        }
    }

}



