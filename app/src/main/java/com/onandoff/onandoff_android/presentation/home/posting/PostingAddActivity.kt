package com.onandoff.onandoff_android.presentation.home.posting


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onandoff.onandoff_android.BuildConfig
import com.onandoff.onandoff_android.databinding.ActivityPostingAddBinding
import com.onandoff.onandoff_android.databinding.BottomsheetSelectPostImageBinding
import com.onandoff.onandoff_android.util.Camera.CAMERA_PERMISSION
import com.onandoff.onandoff_android.util.Camera.FLAG_PERM_CAMERA
import com.onandoff.onandoff_android.util.Camera.FLAG_PERM_STORAGE
import com.onandoff.onandoff_android.util.Camera.STORAGE_PERMISSION
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class PostingAddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingAddBinding
//    private val startForResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                val imgUrl: Uri? = it.data?.data
//                val file: File? = if (uri.scheme == "content") {
//                    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
//                    cursor?.let {
//                        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//                        cursor.moveToFirst()
//                        File(cursor.getString(columnIndex))
//                    }
//                } else {
//                    File(uri.path!!)
//                }
////                binding.iv.setImageURI(imgUrl)
//                Log.d("image", imgUrl.toString())
//                val imgPath = imgUrl?.path
//                imgPath?.let { it1 -> Log.d("image", it1) }
////                imgFile = File(imgPath)
//            }
//        }
    private val startCameraForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imgUrl: Uri? = it.data?.data
//                binding.iv.setImageURI(imgUrl)
                Log.d("image", imgUrl.toString())
                val imgPath = imgUrl?.path
                imgPath?.let { it1 -> Log.d("image", it1) }
//                imgFile = File(imgPath)
                if( it.data?.extras?.get("data") != null){
                    val bitmap =it.data?.extras?.get("data") as Bitmap
                    binding.ivCamera.setImageBitmap(bitmap)
                    val filename = newFileName()
                    val file:File? = saveImgFile(filename,"image/*",bitmap)
                    Log.d("Camera","${file}")
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingAddBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnPostingAdd.setOnClickListener{
            // 게시물 추가
            addPosting()
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
                    0 -> binding.posting?.category = "문화 및 예술"
                    1 -> binding.posting?.category = "스포츠"
                    2 -> binding.posting?.category = "자기계발"
                    3 -> binding.posting?.category = "기타"
                }
            }
            bottomPostingCategoryFragment.show(supportFragmentManager,bottomPostingCategoryFragment.tag)
        }
    }

    private fun addPosting(){
        // TODO : 게시물 추가 API
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
        cameraPickerIntent.type = "image/*"
        Log.d("camera","1")
//        cameraPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
//        launcher.launch(intent);
        startCameraForResult.launch(cameraPickerIntent)
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
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
    fun saveImgFile(filename:String, mimeType:String,bitmap: Bitmap): File? {
        // 파일 선언 -> 경로는 파라미터에서 받는다
        // 파일 선언 -> 경로는 파라미터에서 받는다
        val file: File = File(filename)

        // OutputStream 선언 -> bitmap데이터를 OutputStream에 받아 File에 넣어주는 용도

        // OutputStream 선언 -> bitmap데이터를 OutputStream에 받아 File에 넣어주는 용도
        var out: OutputStream? = null
        try {
            // 파일 초기화
            file.createNewFile()

            // OutputStream에 출력될 Stream에 파일을 넣어준다
            out = FileOutputStream(file)

            // bitmap 압축
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
                Log.d("camera","file 변환 성공 ${file}")
                return file
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return null
    }
    fun newFileName():String{
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return filename
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("camera","req=$requestCode, result = $resultCode, data =$data")
        if(resultCode== Activity.RESULT_OK){
            when(requestCode){
                FLAG_PERM_CAMERA ->{
                        val uri = data?.data // 선택한 이미지의 Uri 객체
                        uri?.let {
                            val inputStream: InputStream? = contentResolver.openInputStream(it)
                            val file = createImageFile()
                            inputStream?.use { input ->
                                FileOutputStream(file).use { output ->
                                    input.copyTo(output)
                                }
                            }

                        }

//                    if( data?.extras?.get("data") != null){
//                        val bitmap = data?.extras?.get("data") as Bitmap
//                        binding.ivCamera.setImageBitmap(bitmap)
//                        val filename = newFileName()
//                        val file:File? = saveImgFile(filename,"image/*",bitmap)
//                        Log.d("Camera","${file}")
//                    }


                }
                FLAG_PERM_STORAGE ->{
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.ivCamera.setImageBitmap(bitmap)
                    val filename = newFileName()
                    val uri = saveImgFile(filename,"image/*",bitmap)

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
                move_gallery()
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
                move_camera()
            }
        }
    }

}

