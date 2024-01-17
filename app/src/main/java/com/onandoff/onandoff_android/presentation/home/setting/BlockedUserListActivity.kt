package com.onandoff.onandoff_android.presentation.home.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.model.BlockedUser
import com.onandoff.onandoff_android.data.model.GetBlockedUserResponse
import com.onandoff.onandoff_android.databinding.ActivityBlockedUserListBinding
import com.onandoff.onandoff_android.presentation.home.setting.viewmodel.BlockedUserListViewModel
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import kotlinx.coroutines.launch

class BlockedUserListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlockedUserListBinding

    private val viewModel by viewModels<BlockedUserListViewModel>(factoryProducer = {
        BlockedUserListViewModel.Factory
    })
    private lateinit var blockedUserListAdapter: BlockedUserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocked_user_list)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupView()
        setupListeners()
        setupViewModel()
    }

    private fun setupView() {
        initRecyclerView(binding.rvBlockedUserList)
    }

    private fun setupListeners() {
        binding.ivBackArrow.setOnClickListener {
            finish()
        }

        supportFragmentManager.setFragmentResultListener(
            UnblockOtherUserDialog.TAG,
            this@BlockedUserListActivity
        ) { _: String, result: Bundle ->
            val action = result.getString(UnblockOtherUserDialog.RESULT_ACTION)
            if (action == UnblockOtherUserDialog.ACTION_UNBLOCK) {
                viewModel.unblockUser(result.getInt(UnblockOtherUserDialog.TO_PROFILE_ID))
            }
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                state.collect { state ->
                    when (state) {
                        is BlockedUserListViewModel.State.GetBlockedUserListFailed -> {
                            when (state.reason) {
                                BlockedUserListViewModel.State.GetBlockedUserListFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(this@BlockedUserListActivity, "db error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        is BlockedUserListViewModel.State.UnblockUserFailed -> {
                            when (state.reason) {
                                BlockedUserListViewModel.State.UnblockUserFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(this@BlockedUserListActivity, "db error", Toast.LENGTH_SHORT).show()
                                }
                                BlockedUserListViewModel.State.UnblockUserFailed.Reason.INVALID_FROM_PROFILE_ID -> {
                                    Toast.makeText(this@BlockedUserListActivity, "invalid from-profile error", Toast.LENGTH_SHORT).show()
                                }
                                BlockedUserListViewModel.State.UnblockUserFailed.Reason.INVALID_TO_PROFILE_ID -> {
                                    Toast.makeText(this@BlockedUserListActivity, "invalid to-profile error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        is BlockedUserListViewModel.State.Idle -> {}
                        is BlockedUserListViewModel.State.GetBlockedUserListSuccess -> {}
                        is BlockedUserListViewModel.State.UnblockUserSuccess -> {
                            val unblockOtherUserConfirmedDialog = UnblockOtherUserConfirmedDialog.newInstance()
                            unblockOtherUserConfirmedDialog.show(supportFragmentManager, UnblockOtherUserConfirmedDialog.TAG)
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        blockedUserListAdapter = BlockedUserListAdapter(
            onButtonClick = {
                openUnblockOtherUserDialog(it)
            }
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = blockedUserListAdapter
        }

        Log.d("initRecyclerView", "blockedUserListAdapter.currentList: ${blockedUserListAdapter.currentList}")
    }

    private fun openUnblockOtherUserDialog(blockedUser: BlockedUser) {
        val unblockOtherUserDialog = UnblockOtherUserDialog.newInstance(blockedUser.profileId)
        unblockOtherUserDialog.show(supportFragmentManager, UnblockOtherUserDialog.TAG)
    }

    companion object {
        fun getIntent(context: Context) =
            Intent(context, BlockedUserListActivity::class.java)
    }
}
