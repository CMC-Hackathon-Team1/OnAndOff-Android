package com.onandoff.onandoff_android.presentation.look

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
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.DialogReportFeedConfirmedBinding

class ReportFeedConfirmedDialog : DialogFragment() {
    private var _binding: DialogReportFeedConfirmedBinding? = null
    private val binding: DialogReportFeedConfirmedBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogReportFeedConfirmedBinding.inflate(inflater, container, false)

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
        binding.btnReportFeedConfirmed.setOnClickListener {
            dismiss()
            requireActivity().finish()
        }
    }

    companion object {
        fun newInstance(): ReportFeedConfirmedDialog =
            ReportFeedConfirmedDialog().apply {
                arguments = Bundle().apply {

                }
            }

        const val TAG = "ReportFeedConfirmedDialog"
    }
}
