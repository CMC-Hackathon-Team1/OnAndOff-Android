package com.onandoff.onandoff_android.presentation.home.persona

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.onandoff.onandoff_android.databinding.DialogCreatePersonaBinding

class CreatePersonaDialog: DialogFragment() {

    private var _binding: DialogCreatePersonaBinding? = null
    private val binding: DialogCreatePersonaBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCreatePersonaBinding.inflate(inflater, container, false)

//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnCreatePersona.setOnClickListener {

        }
    }

    companion object {
        fun newInstance(): CreatePersonaDialog =
            CreatePersonaDialog().apply {
                arguments = Bundle().apply {

                }
            }

        const val TAG = "CreatePersonaDialog"
    }
}