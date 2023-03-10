package com.onandoff.onandoff_android.presentation.look

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.BottomSheetLookAroundFeedOptionMenuBinding

class BottomSheetLookAroundFeedOptionMenu : BottomSheetDialogFragment() {
    private var _binding: BottomSheetLookAroundFeedOptionMenuBinding? = null
    private val binding: BottomSheetLookAroundFeedOptionMenuBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLookAroundFeedOptionMenuBinding.inflate(inflater, container, false)

        setupView()
        setupListeners()

        return binding.root
    }

    override fun getTheme(): Int = R.style.RounderBottomSheetDialog

    private fun setupView() {
        binding.cvFeedListOptionMenu.background = GradientDrawable().apply {
            val radius = resources.getDimension(R.dimen.bottom_sheet_radius)
            cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        }
    }

    private fun setupListeners() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.layoutOptionReport.setOnClickListener {
            val feedId = requireArguments().getInt(FEED_ID)
            val intent = ReportFeedActivity.getIntent(requireActivity(), feedId = feedId)
            startActivity(intent)
            dismiss()
        }
    }

    companion object {
        private const val FEED_ID = "feed_id"

        fun newInstance(feedId: Int): BottomSheetLookAroundFeedOptionMenu =
            BottomSheetLookAroundFeedOptionMenu().apply {
                arguments = Bundle().apply {
                    putInt(FEED_ID, feedId)
                }
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}