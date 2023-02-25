package com.onandoff.onandoff_android.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.databinding.FragmentProfileEditBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID
import com.onandoff.onandoff_android.util.SharePreference
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEditFragment: Fragment() {
    val TAG:String = "MYPAGE"
    private lateinit var binding : FragmentProfileEditBinding
    val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(
        ProfileInterface::class.java)
    val profileId = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,"")
    var profileName = binding.etNickname
    var personaName = binding.etPersonas
    var statusMsg = binding.etOneline
    var profileImg = binding.ivProfileAvatar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMyProfile()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileEditBinding.inflate(layoutInflater)

        binding.tvMypageDelete.setOnClickListener{
        deletePersona()
        }
        return binding.root
    }
    fun getMyProfile() {
        val call = profileInterface?.getMyProfile(profileId)
        call?.enqueue(object: Callback<getMyProfileResponse> {
            override fun onResponse(
                call: Call<getMyProfileResponse>,
                response: Response<getMyProfileResponse>
            ){
                Log.d(TAG,"${response.code()}")
                val body = response.body()
                val originprofileImg = body?.result?.profileImgUrl
                val originprofileName = body?.result?.profileName
                val originstatusMessage = body?.result?.statusMessage
                val originpersonaName = body?.result?.personaName
                binding.apply{
                    etNickname.setText(originprofileName)
                    tvPersonas.text = originpersonaName
                    Glide.with(this@ProfileEditFragment)
                        .load(originprofileImg)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                        .into(binding.ivProfileAvatar)
                    etOneline.setText(originstatusMessage)
                }
            }
            override fun onFailure(call: Call<getMyProfileResponse>, t: Throwable){

            }
        })
    }
    fun editPersona(){
        val call = profileInterface?.profileEidt(profileId, ProfileEditRequest(profileName = profileName.text.toString(),statusMessage=statusMsg.text.toString(),image=profileImg.toString(),defaultImage=true))
        call?.enqueue(object: Callback<ProfileListResultResponse> {
            override fun onResponse(
                call: Call<ProfileListResultResponse>,
                response: Response<ProfileListResultResponse>
            ) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .remove(this@ProfileEditFragment).commit()
            }
            override fun onFailure(call: Call<ProfileListResultResponse>, t: Throwable){

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
                when(response.body()?.statusCode){
                    1500->{
                        Log.d(
                            "Profile Delete",
                            "retrofit manager called, onSucess called but already join!"
                        );
                    }
                    else->{
//
                    }

                }

            }
            override fun onFailure(call: Call<ProfileResult>, t: Throwable){

            }
        })
    }
    }



