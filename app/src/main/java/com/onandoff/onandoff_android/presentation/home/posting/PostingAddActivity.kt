package com.onandoff.onandoff_android.presentation.home.posting


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.FormDataUtil
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.databinding.ActivityPostingAddBinding
import com.onandoff.onandoff_android.databinding.BottomsheetSelectPostImageBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.Camera.CAMERA_PERMISSION
import com.onandoff.onandoff_android.util.Camera.FLAG_PERM_CAMERA
import com.onandoff.onandoff_android.util.Camera.FLAG_PERM_STORAGE
import com.onandoff.onandoff_android.util.Camera.FLAG_REQ_CAMERA
import com.onandoff.onandoff_android.util.Camera.STORAGE_PERMISSION
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*


class PostingAddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingAddBinding
    var categoryId = 0

    var imgFile: MultipartBody.Part? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingAddBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        val profileId = intent.getIntExtra("profileId", -1)

        binding.btnPostingAdd.setOnClickListener{
            // 게시물 추가
            addPosting(profileId)
        }
        binding.ivCamera.setOnClickListener{
            showBottomSheet(this)

        }
        binding.btnBefore.setOnClickListener {
            // 이전으로
            val builder = AlertDialog.Builder(this)
            builder.setMessage("글 쓰기를 취소하시겠습니까?")
                .setPositiveButton("네") { _, _ -> finish()}
                .setNegativeButton("아니오") { _, _ ->}
            builder.show()
        }
        binding.btnCategory.setOnClickListener {
            // 카테고리 선택 fragment 띄우기
            val bottomPostingCategoryFragment = PostingCategoryFragment{
                when (it) {
                    0 -> binding.textCategory.text = "문화 및 예술"
                    1 -> binding.textCategory.text = "스포츠"
                    2 -> binding.textCategory.text = "자기계발"
                    3 -> binding.textCategory.text = "기타"
                }
                categoryId = it
            }
            bottomPostingCategoryFragment.show(supportFragmentManager,bottomPostingCategoryFragment.tag)
        }
    }


    /**
     *  val profiledId: Int,
        val categoryId: Int,
        val hashTagList: List<String>,
        val content: String,
        val isSecret: String
     */
    private fun addPosting(profileId: Int){
        val hashTag = binding.textHashtag.text.toString()
        var hashTagList = hashTag.split("#", " ")
        hashTagList = hashTagList.filter { it.isNotEmpty() }
        val content = binding.textContent.text.toString()
        val isSecret = when(binding.checkboxSecret.isChecked) {
            false -> "PUBLIC"
            true -> "PRIVATE"
        }

        val formProfileId = FormDataUtil.getBody("profileId", profileId)       // 2-way binding 되어 있는 LiveData
        // TODO:CategoryId <- API List에서 가져와서 처리해야함
        val formCategroyId = FormDataUtil.getBody("categoryId", 3)    // 2-way binding 되어 있는 LiveData
        val formHasTagList = FormDataUtil.getBody("hashTagList", hashTagList)
        val formContent = FormDataUtil.getBody("content", content)
    // 2-way binding 되어 있는 LiveData
        val formIsSecret = FormDataUtil.getBody("isSecret", isSecret)    // 2-way binding 되어 있는 LiveData

        Log.d("gallery","$imgFile")


        val feedInterface : FeedInterface? = RetrofitClient.getClient()?.create(FeedInterface::class.java)
        var call = imgFile?.let {
            feedInterface?.addFeedResponse(formProfileId,formCategroyId,formHasTagList,
                it,formContent,formIsSecret)
        }?: run {
            feedInterface?.addFeedResponse(formProfileId,formCategroyId,formHasTagList,
                null,formContent,formIsSecret)
        }

        call?.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                when(response.code()) {
                    200 -> {
                        Log.d("addFeed", "onResponse: Success + ${response.body().toString()}")
                    }
                    else-> Log.d("addFeed","${response.code()}")

                }
                onBackPressed()
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }

        })
    }
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this@PostingAddActivity, "게시글 작성 완료!", Toast.LENGTH_SHORT).show() //토스트 메시지
        val intent =
            Intent(this@PostingAddActivity, MainActivity::class.java) //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        startActivity(intent) //인텐트 이동
        finish() //현재 액티비티 종료
    }
    //갤러리, 기본이미지 변경여부 체크
    fun showBottomSheet(context: Context){

        val dialog = BottomSheetDialog(context)
        val dialogView = BottomsheetSelectPostImageBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(dialogView.root)

        dialog.show()
        dialogView.layoutGallery.setOnClickListener{
            if(checkPermission(STORAGE_PERMISSION, FLAG_PERM_STORAGE) ){
                move_gallery()
            }
            dialog.dismiss()
        }
        dialogView.layoutCamera.setOnClickListener{
            Log.d("image","null 값 선택")
            if(checkPermission(CAMERA_PERMISSION, FLAG_PERM_CAMERA) ){
                move_camera()
            }
            dialog.dismiss()
        }


    }

    fun move_camera() {
        var cameraPickerIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        cameraPickerIntent.type = "image/*"
        startActivityForResult(cameraPickerIntent, FLAG_REQ_CAMERA)
    }

    fun move_gallery() {
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(photoPickerIntent, FLAG_PERM_STORAGE)
    }
    fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
    // 촬영한 사진을 파일로 저장
    fun saveImageToFile(bitmap: Bitmap): File {
        val filename = "image_${System.currentTimeMillis()}.png"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(filename, null, storageDir)
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return imageFile
    }

    private fun getMultipartFromUri(uri: Uri,context:Context): MultipartBody.Part? {
        val filepath = uri.path
        val file = File(filepath)
        var inputStream: InputStream? = null
        try {
            inputStream =  context.contentResolver.openInputStream(uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val bitmap:Bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream)
        var requestBody: RequestBody? = RequestBody.create("image/*".toMediaTypeOrNull(), byteArrayOutputStream.toByteArray())
        val body = requestBody?.let { MultipartBody.Part.createFormData("images", file.name, it) }


        return body
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("gallery","req=$requestCode, result = $resultCode, data =$data")
        if(resultCode== Activity.RESULT_OK){
            when(requestCode){
                //Gallery- 저장소 권한 Flag일때
                FLAG_PERM_STORAGE ->{
                    val uri = data?.data // 선택한 이미지의 Uri 객체
                    binding.ivCamera.setImageURI(uri)
                    val body = uri?.let { getMultipartFromUri(it, context = this) }
                    imgFile = body
                    Log.d("gallery","${imgFile}")
                }
                FLAG_REQ_CAMERA ->{
                  if (data?.extras?.get("data") != null) {
                        //카메라로 방금 촬영한 이미지를 미리 만들어 놓은 이미지뷰로 전달 합니다.
                        val bitmap = data?.extras?.get("data") as Bitmap
                        binding.ivCamera.setImageBitmap(bitmap)
                      val file = saveImageToFile(bitmap)
                      val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                      imgFile = MultipartBody.Part.createFormData("images", file.name, requestFile)

                    }

                }

            }

        }
    }

    //checkPermission() 에서 ActivityCompat.requestPermissions 을 호출한 다음 사용자가 권한 허용여부를 선택하면 해당 메소드로 값이 전달 됩니다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            FLAG_PERM_CAMERA -> {
                Log.d("permission","2")
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인하지않으면 앱을 실행할수없습니다", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        return
                    }
                }
                move_camera()

            }
            FLAG_PERM_STORAGE -> {
                Log.d("permission","2")
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인하지않으면 앱을 실행할수없습니다", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        return
                    }
                }
                move_gallery()

            }
        }
    }

}

