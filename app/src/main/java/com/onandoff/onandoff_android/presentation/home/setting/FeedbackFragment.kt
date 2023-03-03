package com.onandoff.onandoff_android.presentation.home.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.ProfileResult
import com.onandoff.onandoff_android.databinding.FragmentFeedbackBinding
import com.onandoff.onandoff_android.databinding.FragmentSettingBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackFragment: Fragment() {
    private lateinit var binding:FragmentFeedbackBinding
    val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(
        ProfileInterface::class.java)
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackBinding.inflate(layoutInflater)
        binding.tvFeedbackSubmit.setOnClickListener {
            sendFeedback(binding.etFeedbackText.text.toString())
            finish()
        }
        return binding.root
    }

    fun sendFeedback(feedText:String){
        val call = profileInterface?.sendFeedBack()
        call?.enqueue(object: Callback<ProfileResult> {
            override fun onResponse(
                call: Call<ProfileResult>,
                response: Response<ProfileResult>
            ){


            }
            override fun onFailure(call: Call<ProfileResult>, t: Throwable){
                Toast.makeText(mainActivity,"통신에 실패했습니다${t} 잠시후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        })
    }

}