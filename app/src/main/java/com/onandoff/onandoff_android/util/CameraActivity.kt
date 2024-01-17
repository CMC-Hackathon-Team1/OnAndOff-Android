//package com.onandoff.onandoff_android.util
//
//
//import android.net.Uri
//import android.provider.MediaStore
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//
//
//class CameraActivity:AppCompatActivity(){
//    fun getPathFromUri(uri: Uri): String {
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = contentResolver.query(uri, projection, null, null, null) ?: return uri.path ?: ""
//        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor.moveToFirst()
//        val path = cursor.getString(columnIndex)
//        cursor.close()
//        return path
//    }
//}
