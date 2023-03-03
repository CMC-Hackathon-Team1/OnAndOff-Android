package com.onandoff.onandoff_android.presentation.mypage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.onandoff.onandoff_android.databinding.FragmentProfileEditBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID
import com.onandoff.onandoff_android.util.Camera
import com.onandoff.onandoff_android.util.SharePreference
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEditFragment: Fragment() {
    //권한 가져오기
    var imgFile: MultipartBody.Part? = null
    var isBasicImage:Boolean = false
    val TAG:String = "MYPAGE"
    var feedLength:Int = 0
    private lateinit var binding : FragmentProfileEditBinding
    val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(
        ProfileInterface::class.java)
    val profileId = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,0)
    lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileEditBinding.inflate(layoutInflater)

        var personaName = arguments?.getBundle("personaName")
        var nickName = arguments?.getBundle("nickName")
        var profileImg = arguments?.getBundle("profileImg")
        var statusMsg = arguments?.getBundle("statusMsg")
        binding.tvPersonas.text = personaName.toString()
        binding.tvNickname.text = nickName.toString()
        binding.ivProfileAvatar.setImageUrl(profileImg.toString())
        binding.tvOneline.text = statusMsg.toString()
        binding.ivProfileAvatar.setOnClickListener{
            showBottomSheet(mainActivity)
        }
        binding.tvMypageDelete.setOnClickListener{
            deletePersona()
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
                feedLength = response.body()?.result?.size ?: 0

            }
            override fun onFailure(call: Call<ProfileListResponse>, t: Throwable){

            }
        })
    }
    fun editPersona(){
        var etProfileName = binding.etNickname.text
        var etStatusMsg = binding.etOneline.text
        var etProfileImg = binding.ivProfileAvatar
    // TODO : call lateinit 시 초기홥아법
        lateinit var call :Call<ProfileListResultResponse>
        if(imgFile !=null) {
            call = profileInterface?.profileEdit(profileId,
                FormDataUtil.getBody("profileName", etProfileName.toString()) , FormDataUtil.getBody("statusMessage", etStatusMsg.toString()),image= imgFile,defaultImage= FormDataUtil.getBody("statusMessage", isBasicImage))!!
        }else{
            call = profileInterface?.profileEdit(profileId,
                FormDataUtil.getBody("profileName", etProfileName.toString()) , FormDataUtil.getBody("statusMessage", etStatusMsg.toString()),defaultImage= FormDataUtil.getBody("statusMessage", isBasicImage))!!
        }
        call?.enqueue(object: Callback<ProfileListResultResponse> {
            override fun onResponse(
                call: Call<ProfileListResultResponse>,
                response: Response<ProfileListResultResponse>
            ) {
                Toast.makeText(mainActivity,"프로필 편집이 성공했습니다",Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()
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
                if(feedLength<=1){
                   //TODO : showDialog()
                   //start Intent to createActivity

                }else{
                    //TODO : showDialog()
                    //start Intent to mainActivity
                }

            }
            override fun onFailure(call: Call<ProfileResult>, t: Throwable){

            }
        })
    }
    }



