package com.onandoff.onandoff_android.data.remote

import com.onandoff.onandoff_android.data.model.BlockOtherUserResponse
import com.onandoff.onandoff_android.data.model.GetBlockedUserListResponse
import com.onandoff.onandoff_android.data.request.BlockOrUnblockOtherUserRequest

interface UserRemoteDataSource {
    suspend fun blockOrUnblockOtherUser(blockOrUnblockOtherUserRequest: BlockOrUnblockOtherUserRequest): BlockOtherUserResponse

    suspend fun getBlockedUserList(profileId: Int): GetBlockedUserListResponse
}