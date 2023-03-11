package com.onandoff.onandoff_android.presentation.home.posting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.FormDataUtil
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedData
import com.onandoff.onandoff_android.data.model.FeedReadData
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.databinding.ActivityPostingModifyBinding
import com.onandoff.onandoff_android.util.Camera
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostingModifyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingModifyBinding
    private val feedInterface : FeedInterface? = RetrofitClient.getClient()?.create(FeedInterface::class.java)
    var categoryId = 0
    var imgFile: MultipartBody.Part? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingModifyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val feedId = intent.getIntExtra("feedId",-1)
        val profileId = intent.getIntExtra("profileId", -1)

        // 기존 게시물 내용 불러오기
        getPostingByFeedId(profileId,feedId)

        binding.btnPostingModify.setOnClickListener{
            val hashTag = binding.textHashtag.text.toString()
            var hashTagList = hashTag.split(" ", "#")
            hashTagList = hashTagList.filter { it.isNotEmpty() }

            val content = binding.textContent.text.toString()
            val isSecret = when(binding.checkboxSecret.isChecked) {
                false -> "PUBLIC"
                true -> "PRIVATE"
            }

            if (profileId == -1) {
                finish()
            } else if (hashTagList.isEmpty()) {
                Toast.makeText(this@PostingModifyActivity, "해쉬태그를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (content.isEmpty() || content == "") {
                Toast.makeText(this@PostingModifyActivity, "피드의 내용을 입력해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else if (categoryId == 0) {
                Toast.makeText(this@PostingModifyActivity, "피드의 카테고리를 선택해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // 게시물 추가
                modifyPosting(profileId, feedId, hashTagList, content, isSecret)
            }
        }
        binding.ivCamera.setOnClickListener{
         move_gallery()
        }
        binding.btnBefore.setOnClickListener {
            // 이전으로
            val builder = AlertDialog.Builder(this)
            builder.setMessage("글 수정을 취소하시겠습니까?")
                .setPositiveButton("네") { _, _ -> finish()}
                .setNegativeButton("아니오") { _, _ ->}
            builder.show()
        }
        binding.categoryLayout.setOnClickListener {
            // 카테고리 선택 fragment 띄우기
            val bottomPostingCategoryFragment = PostingCategoryFragment{
                when (it) {
                    1 -> binding.textCategory.text = "문화 및 예술"
                    2 -> binding.textCategory.text = "스포츠"
                    3 -> binding.textCategory.text = "자기계발"
                    4 -> binding.textCategory.text = "기타"
                }
                categoryId = it
            }
            bottomPostingCategoryFragment.show(supportFragmentManager,bottomPostingCategoryFragment.tag)
        }
    }

    private fun getPostingByFeedId(profileId: Int,feedId: Int) {
        val call = feedInterface?.readFeedResponse(feedId, profileId)
        call?.enqueue(object : Callback<FeedReadData> {
            override fun onResponse(call: Call<FeedReadData>, response: Response<FeedReadData>) {
                when(response.code()) {
                    200 -> {
                        Log.d("readFeed", "onResponse: Success + ${response.body()!!.hashTagList}")
                        if(response.body()!=null) {
                            Glide.with(this@PostingModifyActivity)
                                .load(response.body()!!.feedImgList[0])
                                .into(binding.ivCamera)
                            binding.textContent.setText(response.body()!!.feedContent)
                            val hashTagList = response.body()!!.hashTagList
                            var tagText = ""
                            for (tag in hashTagList) {
                                tagText = "#$tag $tagText"
                            }
                            binding.textHashtag.setText(tagText)
                            when(response.body()!!.categoryId) {
                                0 -> binding.textCategory.text = "카테고리"
                                1 -> binding.textCategory.text = "문화 및 예술"
                                2 -> binding.textCategory.text = "스포츠"
                                3 -> binding.textCategory.text = "자기계발"
                                4 -> binding.textCategory.text = "기타"
                            }
                            categoryId = response.body()!!.categoryId
                            binding.posting = response.body()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FeedReadData>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }
        })
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
                    binding.ivCamera.setImageURI(uri)
                    val filePath = uri?.let { getPathFromUri(it,this@PostingModifyActivity) }
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
                        this@PostingModifyActivity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(this@PostingModifyActivity, permissions, flag)
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
                        Toast.makeText(this@PostingModifyActivity, "저장소 권한을 승인하지않으면 앱을 실행할수없습니다", Toast.LENGTH_SHORT)
                            .show()
//                        finish()
                        return
                    }
                }
                move_gallery()
            }

        }
    }

    private fun modifyPosting(profileId: Int,feedId: Int, hashTagList: List<String>, content:String, isSecret: String){
        val formProfileId =
            FormDataUtil.getBody("profileId", profileId)
        val formfeedId =
            FormDataUtil.getBody("feedId", feedId)  // 2-way binding 되어 있는 LiveData
        val formCategoryId =
            FormDataUtil.getBody("categoryId", categoryId)    // 2-way binding 되어 있는 LiveData
        val formHasTagList = ArrayList<MultipartBody.Part>()
        for (item in hashTagList) {
            formHasTagList.add(FormDataUtil.getBody("hashTagList", item))
        }
        val formContent = FormDataUtil.getBody("content", content)
        // 2-way binding 되어 있는 LiveData
        val formIsSecret =
            FormDataUtil.getBody("isSecret", isSecret)

        var call = imgFile?.let {
            feedInterface?.updateFeedResponse(
                formProfileId, formCategoryId, formfeedId,formHasTagList,
                it, formContent, formIsSecret
            )
        } ?: run {
            feedInterface?.updateFeedResponse(
                formProfileId, formCategoryId, formfeedId,formHasTagList,
                null, formContent, formIsSecret
            )
        }
        call?.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                when(response.code()) {
                    200 -> {
                        Log.d("updateFeed", "onResponse: ${response.body().toString()}")
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }
        })
    }
}