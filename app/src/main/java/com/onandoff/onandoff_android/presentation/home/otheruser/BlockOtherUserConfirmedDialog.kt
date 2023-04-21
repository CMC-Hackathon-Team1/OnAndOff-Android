package com.onandoff.onandoff_android.presentation.home.otheruser

import android.content.DialogInterface
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
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.DialogBlockOtherUserConfirmedBinding
import com.onandoff.onandoff_android.presentation.look.LookAroundFragment

class BlockOtherUserConfirmedDialog: DialogFragment() {
    private var _binding: DialogBlockOtherUserConfirmedBinding? = null
    private val binding: DialogBlockOtherUserConfirmedBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBlockOtherUserConfirmedBinding.inflate(inflater, container, false)

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
        binding.btnUnblockOtherUserConfirmed.setOnClickListener {
            dismiss()
            val fragment = requireActivity().supportFragmentManager.fragments.find { it is LookAroundFragment }!!
            fragment.onResume()
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fcv_main,
                fragment
            ).commit()
//            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    companion object {
        fun newInstance(): BlockOtherUserConfirmedDialog =
            BlockOtherUserConfirmedDialog().apply {
                arguments = Bundle().apply {

                }
            }

        const val TAG = "BlockOtherUserConfirmedDialog"
    }
}