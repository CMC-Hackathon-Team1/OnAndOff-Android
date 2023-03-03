package com.onandoff.onandoff_android.presentation.look

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.databinding.BottomSheetFeedListOptionMenuBinding

class BottomSheetFeedListOptionMenu : BottomSheetDialogFragment() {
    private var _binding: BottomSheetFeedListOptionMenuBinding? = null
    private val binding: BottomSheetFeedListOptionMenuBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetFeedListOptionMenuBinding.inflate(inflater, container, false)

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
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

        fun newInstance(feedId: Int): BottomSheetFeedListOptionMenu =
            BottomSheetFeedListOptionMenu().apply {
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