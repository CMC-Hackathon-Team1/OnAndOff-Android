package com.onandoff.onandoff_android.presentation.home.otheruser

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.BottomSheetBlockOtherUserOptionMenuBinding
import com.onandoff.onandoff_android.presentation.home.persona.CreatePersonaDialog

class BottomSheetBlockOtherUserOptionMenu : BottomSheetDialogFragment() {
    private var _binding: BottomSheetBlockOtherUserOptionMenuBinding? = null
    private val binding: BottomSheetBlockOtherUserOptionMenuBinding
        get() = _binding!!

    private val viewModel : BlockOtherUserViewModel by activityViewModels(factoryProducer = {
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

//        binding.layoutOptionReportUser.setOnClickListener {
//            val userId = requireArguments().getInt(USER_ID)
//            val intent = BlockOtherUserActivity.getIntent(requireActivity(), userId = userId)
//            startActivity(intent)
//            dismiss()
//        }

        binding.layoutOptionBlockUser.setOnClickListener {
            val otherUserID = requireArguments().getInt(USER_ID)
//            viewModel.blockOtherUser(otherUserID)

            val createPersonaDialog = BlockOtherUserConfirmedDialog.newInstance()
            createPersonaDialog.show(childFragmentManager, BlockOtherUserConfirmedDialog.TAG)
        }
    }

    companion object {
        private const val USER_ID = "user_id"

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