//package com.onandoff.onandoff_android.util
//
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.onandoff.onandoff_android.util.Camera.FLAG_PERM_CAMERA
//import com.onandoff.onandoff_android.util.Camera.FLAG_PERM_STORAGE
//
//
//class CameraActivity:AppCompatActivity() {
//
//    fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
//        Log.d("permission", "실행됨?")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Log.d("permission", "1")
//            for (permission in permissions) {
//                //만약 권한이 승인되어 있지 않다면 권한승인 요청을 사용에 화면에 호출합니다.
//                if (ContextCompat.checkSelfPermission(
//                        this@CameraActivity,
//                        permission
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//
//                    ActivityCompat.requestPermissions(this@CameraActivity, permissions, flag)
//                    return false
//                }
//            }
//        }
//        return true
//    }
//    //checkPermission() 에서 ActivityCompat.requestPermissions 을 호출한 다음 사용자가 권한 허용여부를 선택하면 해당 메소드로 값이 전달 됩니다.
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            FLAG_PERM_STORAGE -> {
//                for (grant in grantResults) {
//                    if (grant != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "저장소 권한을 승인하지않으면 앱을 실행할수없습니다", Toast.LENGTH_SHORT)
//                            .show()
//                        finish()
//                        return
//                    }
//                }
//                //카메라 호출 메소드
//                setViews()
//            }
//            FLAG_PERM_CAMERA -> {
//                for (grant in grantResults) {
//                    if (grant != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "카메라 권한을 승인해야지만 카메라를 사용할 수 있습니다.", Toast.LENGTH_SHORT)
//                            .show()
//                        return
//                    }
//                }
//                openCamera()
//            }
//        }
//    }
//}