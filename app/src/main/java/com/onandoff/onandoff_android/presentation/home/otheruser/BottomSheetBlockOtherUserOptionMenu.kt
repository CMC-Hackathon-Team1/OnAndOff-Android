package com.onandoff.onandoff_android.presentation.home.otheruser

import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.BottomSheetBlockOtherUserOptionMenuBinding
import com.onandoff.onandoff_android.presentation.home.setting.UnblockOtherUserDialog

class BottomSheetBlockOtherUserOptionMenu : BottomSheetDialogFragment() {
    private var _binding: BottomSheetBlockOtherUserOptionMenuBinding? = null
    private val binding: BottomSheetBlockOtherUserOptionMenuBinding
        get() = _binding!!

    private val viewModel by activityViewModels<BlockOtherUserViewModel>(factoryProducer = {
        BlockOtherUserViewModel.Factory
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBlockOtherUserOptionMenuBinding.inflate(inflater, container, false)

        setupView()
        setupListeners()

        return binding.root
    }

    override fun getTheme(): Int = R.style.RounderBottomSheetDialog

    private fun setupView() {
        binding.cvOtherUserOptionMenu.background = GradientDrawable().apply {
            val radius = resources.getDimension(R.dimen.bottom_sheet_radius)
            cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        }
    }

    private fun setupListeners() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.layoutOptionBlockUser.setOnClickListener {
            val otherUserID = requireArguments().getInt(USER_ID)
            viewModel.blockOtherUser(otherUserID)
            setFragmentResult(TAG, Bundle().apply {
                putString(
                    RESULT_ACTION,
                    ACTION_BLOCK
                )
            })
            dismiss()
        }
    }

    companion object {
        private const val USER_ID = "user_id"
        const val RESULT_ACTION = "result_action"
        const val ACTION_BLOCK = "action_block"
        const val TAG = "BottomSheetBlockOtherUserOptionMenu"

        fun newInstance(userId: Int): BottomSheetBlockOtherUserOptionMenu =
            BottomSheetBlockOtherUserOptionMenu().apply {
                arguments = Bundle().apply {
                    putInt(USER_ID, userId)
                }
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}