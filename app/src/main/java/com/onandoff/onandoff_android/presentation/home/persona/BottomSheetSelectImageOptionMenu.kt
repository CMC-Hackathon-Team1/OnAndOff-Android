package com.onandoff.onandoff_android.presentation.home.persona

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.BottomSheetSelectImageOptionMenuBinding
import com.onandoff.onandoff_android.presentation.home.viewmodel.CreatePersonaViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class BottomSheetSelectImageOptionMenu : BottomSheetDialogFragment() {
    private var _binding: BottomSheetSelectImageOptionMenuBinding? = null
    private val binding: BottomSheetSelectImageOptionMenuBinding
        get() = _binding!!

    private val viewModel by activityViewModels<CreatePersonaViewModel>(factoryProducer = {
        CreatePersonaViewModel.Factory
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectImageOptionMenuBinding.inflate(inflater, container, false)

        setupView()
        setupListeners()

        return binding.root
    }

    override fun getTheme(): Int = R.style.RounderBottomSheetDialog

    private fun setupView() {
        binding.cvSelectImageOptionMenu.background = GradientDrawable().apply {
            val radius = resources.getDimension(R.dimen.bottom_sheet_radius)
            cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        }
    }

    private fun setupListeners() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.layoutOptionDefaultImage.setOnClickListener {
            dismiss()
        }

        binding.layoutSearchAlbum.setOnClickListener {
            if (checkStoragePermission()) {
                openGallery()
            }
        }
    }

    // CreatePersonaActivity -> ImageChooser
    // ImageChooser -> CreatePersonaActivity

    // CreatePersonaActivity -> BottomSheetSelectImageOptionMenu -> ImageChooser
    // ImageChooser -> BottomSheetSelectImageOptionMenu -> CreatePersonaActivity


    private fun openGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        openGalleryResultLauncher.launch(pickIntent)
    }

    private val openGalleryResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val imagePath = it.data?.data

                val file = File(absolutelyPath(imagePath, requireActivity()))
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("profile", file.name, requestFile)

                viewModel.setPersonaImagePath(absolutelyPath(imagePath, requireActivity()))
                Log.d(
                    "viewModel.setPersonaImagePath",
                    "viewModel.setPersonaImagePath: $file.absolutePath"
                )
                dismiss()
            }
        }

    // 절대 경로 변환
    private fun absolutelyPath(path: Uri?, context: Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()

        val result = cursor?.getString(index!!)

        return result!!
    }

    private fun checkStoragePermission(): Boolean {
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        return if (ActivityCompat.checkSelfPermission(requireActivity(), readPermission)
            == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireActivity(), writePermission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(readPermission, writePermission),
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

        fun newInstance(): BottomSheetSelectImageOptionMenu = BottomSheetSelectImageOptionMenu()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
