package com.onandoff.onandoff_android.presentation.home.persona

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.DialogCreatePersonaBinding

class CreatePersonaDialog : DialogFragment() {

    private var _binding: DialogCreatePersonaBinding? = null
    private val binding: DialogCreatePersonaBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCreatePersonaBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setupView()
        setupListeners()

        return binding.root
    }

    private fun setupView() {
        binding.buttonPanel.background = GradientDrawable().apply {
            val radius = resources.getDimension(R.dimen.create_persona_dialog_radius)
            cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius)

            val strokeWidth =
                resources.getDimensionPixelSize(R.dimen.create_persona_dialog_stroke_width)
            setStroke(strokeWidth, ContextCompat.getColor(requireContext(), R.color.color_main))
        }
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnCreatePersona.setOnClickListener {
            // dialog -> activity
            setFragmentResult(TAG, Bundle().apply {
                putString(RESULT_ACTION, ACTION_CREATE)
            })
            dismiss()
        }
    }

    companion object {
        const val RESULT_ACTION = "result_action"
        const val ACTION_CREATE = "action_create"

        fun newInstance(): CreatePersonaDialog =
            CreatePersonaDialog().apply {
                arguments = Bundle().apply {

                }
            }

        const val TAG = "CreatePersonaDialog"
    }
}
