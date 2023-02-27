package com.onandoff.onandoff_android.presentation.home.persona

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivityCreatePersonaBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.viewmodel.CreatePersonaViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CreatePersonaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePersonaBinding
    private val viewModel by viewModels<CreatePersonaViewModel>(factoryProducer = {
        CreatePersonaViewModel.Factory
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePersonaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                state.collect { state ->
                    when (state) {
                        is CreatePersonaViewModel.State.CreateFailed -> {
                            when (state.reason) {
                                CreatePersonaViewModel.State.CreateFailed.Reason.EMPTY_PERSONA_NAME -> {
                                    binding.viewPersonas.setBackgroundColor(getColor(R.color.errorColor))
                                    binding.tvPersonasError.setTextColor(getColor(R.color.errorColor))
                                }
                                CreatePersonaViewModel.State.CreateFailed.Reason.EMPTY_PROFILE_NAME -> {
                                    binding.viewNickname.setBackgroundColor(getColor(R.color.errorColor))
                                    binding.tvPersonasError.setTextColor(getColor(R.color.errorColor))
                                }
                                CreatePersonaViewModel.State.CreateFailed.Reason.LIMIT_PROFILE_COUNT -> {
                                    Toast.makeText(
                                        this@CreatePersonaActivity,
                                        "프로필은 3개까지 생성 가능합니다.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                else -> {}
                            }
                        }
                        CreatePersonaViewModel.State.Idle -> {
                        }
                        CreatePersonaViewModel.State.Success -> {
                            Toast.makeText(
                                this@CreatePersonaActivity,
                                "프로필 생성 성공!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent =
                                Intent(this@CreatePersonaActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.ivBackArrow.setOnClickListener {
            finish()
        }
        binding.ivPersonaProfile.setOnClickListener {
            if (checkStoragePermission()) {
                openGallery()
            }
        }
        binding.tvFinish.setOnClickListener {
            viewModel.onCreatePersona()
        }
    }
    private fun openGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        openGalleryResultLauncher.launch(pickIntent)
    }

    private val openGalleryResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val imagePath = it.data?.data

                val file = File(absolutelyPath(imagePath, this))
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("profile", file.name, requestFile)

                viewModel.setPersonaImagePath(absolutelyPath(imagePath, this))
                Log.d("viewModel.setPersonaImagePath", "viewModel.setPersonaImagePath: $file.absolutePath")
            }
        }

    // 절대 경로 변환
    private fun absolutelyPath(path: Uri?, context : Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()

        val result = cursor?.getString(index!!)

        return result!!
    }

    private fun checkStoragePermission(): Boolean {
        val cameraPermission = Manifest.permission.CAMERA
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        return if (ActivityCompat.checkSelfPermission(this, cameraPermission)
            == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, readPermission)
            == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, writePermission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(cameraPermission, readPermission, writePermission),
                PERMISSION_REQ_CODE
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        }
    }

    companion object {
        const val PERMISSION_REQ_CODE = 1010

        fun getIntent(context: Context) =
            Intent(context, CreatePersonaActivity::class.java)
    }
}