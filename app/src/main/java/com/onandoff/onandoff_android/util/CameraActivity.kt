package com.onandoff.onandoff_android.util


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.onandoff.onandoff_android.databinding.ActivityProfileCreateBinding

import com.onandoff.onandoff_android.util.Camera
import com.onandoff.onandoff_android.util.Camera.CAMERA_PERMISSION
import com.onandoff.onandoff_android.util.Camera.FLAG_REQ_CAMERA
import com.onandoff.onandoff_android.util.Camera.PERM_CAMERA
import com.onandoff.onandoff_android.util.Camera.PERM_STORAGE
import com.onandoff.onandoff_android.util.Camera.STORAGE_PERMISSION


class CameraActivity:AppCompatActivity()  {
    private lateinit var binding: ActivityProfileCreateBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //저장소 권한체크
        if (checkPermission(STORAGE_PERMISSION, PERM_STORAGE)) {
            setViews()
        }
    }

    private fun setViews() {
        //카메라 버튼을 클릭하면
        binding.ivAddBackground.setOnClickListener {
            //카메라 열기
            openCamera()
        }
    }

    private fun openCamera() {
        //카메라 권한 확인
        if (checkPermission(CAMERA_PERMISSION, PERM_CAMERA)) {
            //권한이 있으면 카메라를 실행시킵니다.
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, FLAG_REQ_CAMERA)
        }
    }

    //권한 체크 메소드
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

    //checkPermission() 에서 ActivityCompat.requestPermissions 을 호출한 다음 사용자가 권한 허용여부를 선택하면 해당 메소드로 값이 전달 됩니다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERM_STORAGE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인하지않으면 앱을 실행할수없습니다", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        return
                    }
                }
                //카메라 호출 메소드
                setViews()
            }
            PERM_CAMERA -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인해야지만 카메라를 사용할 수 있습니다.", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }
                }
                openCamera()
            }
        }
    }

    //startActivityForResult 을 사용한 다음 돌아오는 결과값을 해당 메소드로 호출합니다.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                FLAG_REQ_CAMERA -> {
                    if (data?.extras?.get("data") != null) {
                        //카메라로 방금 촬영한 이미지를 미리 만들어 놓은 이미지뷰로 전달 합니다.
                        val bitmap = data?.extras?.get("data") as Bitmap
                        binding.ivProfileBackground.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}
