package com.onandoff.onandoff_android.presentation.home.otheruser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivityBlockOtherUserBinding
import kotlinx.coroutines.launch

class BlockOtherUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlockOtherUserBinding

//    private val viewModel by viewModels<BlockOtherUserViewModel>(factoryProducer = {
//        BlockOtherUserViewModel.Factory
//    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlockOtherUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.lifecycleOwner = this
//        binding.viewModel = viewModel

//        setupViewModel()
        setupListeners()
    }

//    private fun setupViewModel() {
//        with(viewModel) {
//            lifecycleScope.launch {
//                state.collect { state ->
//                    when (state) {
//                        is BlockOtherUserViewModel.State.BlockOtherUserFailed -> {
//                            when (state.reason) {
//                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.PARAMETER_ERROR -> {
//                                    Toast.makeText(
//                                        this@BlockOtherUserActivity,
//                                        "parameter error",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.JWT_ERROR -> {
//                                    Toast.makeText(
//                                        this@BlockOtherUserActivity,
//                                        "jwt error",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.DB_ERROR -> {
//                                    Toast.makeText(
//                                        this@BlockOtherUserActivity,
//                                        "db error",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.NO_FEED -> {
//                                    Toast.makeText(
//                                        this@BlockOtherUserActivity,
//                                        "해당 유저가 존재하지 않습니다.",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.ALREADY_REPORTED -> {
//                                    Toast.makeText(
//                                        this@BlockOtherUserActivity,
//                                        "이미 신고된 유저입니다.",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.INVALID_REPORT_CATEGORY_ID -> {
//                                    Toast.makeText(
//                                        this@BlockOtherUserActivity,
//                                        "올바르지 않은 신고 분류입니다.",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.NO_REPORT_ETC_REASON -> {
//                                    Toast.makeText(
//                                        this@BlockOtherUserActivity,
//                                        "기타 신고 사유를 입력하지 않았습니다.",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                                else -> {}
//                            }
//                        }
//                        BlockOtherUserViewModel.State.Idle -> {}
//                        BlockOtherUserViewModel.State.BlockOtherUserSuccess -> {
//                            val reportUserConfirmedDialog = BlockOtherUserConfirmedDialog.newInstance()
//                            reportUserConfirmedDialog.show(supportFragmentManager, BlockOtherUserConfirmedDialog.TAG)
//                        }
//                    }
//                }
//            }
//        }
//    }

    private val checkBoxList by lazy {
        listOf(
            binding.checkboxSpamPromotion,
            binding.checkboxPorn,
            binding.checkboxDespise,
            binding.checkboxDobae,
            binding.checkboxPrivateInfoIllegalInfo,
            binding.checkboxEtc
        )
    }

    private fun setupListeners() {
        checkBoxListeners()

        binding.ivBackArrow.setOnClickListener {
            finish()
        }

        binding.btnAgree.setOnClickListener {
            val reportUserDialog = BlockOtherUserDialog.newInstance()
            reportUserDialog.show(supportFragmentManager, BlockOtherUserDialog.TAG)
        }

//        supportFragmentManager.setFragmentResultListener(
//            BlockOtherUserDialog.TAG,
//            this@BlockOtherUserActivity
//        ) { _: String, result: Bundle ->
//            val action = result.getString(BlockOtherUserDialog.RESULT_ACTION)
//            if (action == BlockOtherUserDialog.ACTION_BLOCK) {
//                val reportCategoryId = checkBoxList.indexOfFirst { it.isChecked } + 1
//                if (reportCategoryId >= 1) {
//                    val content =
//                        if (binding.checkboxEtc.isChecked) {
//                            binding.editEtc.text.toString()
//                        } else {
//                            null
//                        }
//
//                    viewModel.blockOtherUser(reportCategoryId, content)
//                }
//            }
//        }
    }

    private fun checkBoxListeners() {
        binding.checkboxSpamPromotion.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
                binding.btnAgree.isEnabled = true
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_round_main_corner_main_background)
            } else {
                binding.btnAgree.isEnabled = false
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_disable)
            }
        }

        binding.checkboxPorn.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
                binding.btnAgree.isEnabled = true
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_round_main_corner_main_background)
            } else {
                binding.btnAgree.isEnabled = false
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_disable)
            }
        }

        binding.checkboxDespise.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
                binding.btnAgree.isEnabled = true
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_round_main_corner_main_background)
            } else {
                binding.btnAgree.isEnabled = false
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_disable)
            }
        }

        binding.checkboxDobae.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
                binding.btnAgree.isEnabled = true
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_round_main_corner_main_background)
            } else {
                binding.btnAgree.isEnabled = false
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_disable)
            }
        }

        binding.checkboxPrivateInfoIllegalInfo.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxSpamPromotion.isChecked = false
                binding.checkboxEtc.isChecked = false
                binding.editEtc.isEnabled = false
                binding.btnAgree.isEnabled = true
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_round_main_corner_main_background)
            } else {
                binding.btnAgree.isEnabled = false
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_disable)
            }
        }

        binding.checkboxEtc.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.checkboxPorn.isChecked = false
                binding.checkboxDespise.isChecked = false
                binding.checkboxDobae.isChecked = false
                binding.checkboxPrivateInfoIllegalInfo.isChecked = false
                binding.checkboxSpamPromotion.isChecked = false
                binding.editEtc.isEnabled = true
                binding.btnAgree.isEnabled = true
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_round_main_corner_main_background)

                if (binding.editEtc.text.length > 20) {
                    Toast.makeText(
                        this@BlockOtherUserActivity,
                        "글자 수는 최대 20자까지 가능합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                binding.editEtc.text.clear()
                binding.editEtc.isEnabled = false
                binding.btnAgree.isEnabled = false
                binding.btnAgree.background = ContextCompat.getDrawable(this, R.drawable.button_disable)
            }
        }
    }

    companion object {
        fun getIntent(context: Context, userId: Int) =
            Intent(context, BlockOtherUserActivity::class.java)
                .putExtra(BlockOtherUserViewModel.USER_ID, userId)
    }
}