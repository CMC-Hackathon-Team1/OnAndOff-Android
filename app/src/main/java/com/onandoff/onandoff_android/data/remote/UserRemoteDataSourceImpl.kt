package com.onandoff.onandoff_android.data.remote

import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.model.BlockOtherUserResponse
import com.onandoff.onandoff_android.data.model.GetBlockedUserListResponse
import com.onandoff.onandoff_android.data.request.BlockOrUnblockOtherUserRequest

class UserRemoteDataSourceImpl(
    private val userInterface: UserInterface
): UserRemoteDataSource {
    override suspend fun blockOrUnblockOtherUser(blockOrUnblockOtherUserRequest: BlockOrUnblockOtherUserRequest): BlockOtherUserResponse {
        return userInterface.blockOrUnblockOtherUser(blockOrUnblockOtherUserRequest)
    }

    override suspend fun getBlockedUserList(profileId: Int): GetBlockedUserListResponse {
        return userInterface.getBlockedUserList(profileId)
    }
}
