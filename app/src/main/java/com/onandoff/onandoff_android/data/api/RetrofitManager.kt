package com.onandoff.onandoff_android.data.api
import android.util.Log
import com.onandoff.onandoff_android.data.model.UserSignUp
import retrofit2.*

class RetrofitManager {
    companion object{
        val instance= RetrofitManager()
    }

    private val iSignUp:ISignUp? = RetrofitClient.getClient(API.BASE_URL)?.create(ISignUp::class.java)
    fun signUp(userSignUp: UserSignUp,completion:(UserSignUp)->Unit){
//        val userA =UserSignUp("s202000529@hufs-gsuite.kr","kimwest00")
        val user = userSignUp.let{it}?: UserSignUp("","")
        val call = iSignUp?.signUp(userSignUp=user).let{
            it
        }?:return

        call.enqueue(object: retrofit2.Callback<UserSignUp>{
            override fun onResponse(call: Call<UserSignUp>, response: Response<UserSignUp>) {
                if(response.isSuccessful){
                    Log.d("Post","retrofit manager called, onSucess called ${response.body()}")
                }
                else {
                    Log.d("Post", "retrofit manager called, onSucess called,but ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<UserSignUp>, t: Throwable) {
                Log.d("Post","retrofit manager called,but onFailure called | ${t}")
            }
        })

}
}
