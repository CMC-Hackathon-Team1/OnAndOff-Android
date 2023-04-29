package com.onandoff.onandoff_android.data.repository

import com.onandoff.onandoff_android.data.model.BlockOtherUserResponse
import com.onandoff.onandoff_android.data.model.BlockedUser
import com.onandoff.onandoff_android.data.model.GetBlockedUserListResponse
import com.onandoff.onandoff_android.data.model.toEntity
import com.onandoff.onandoff_android.data.remote.UserRemoteDataSource
import com.onandoff.onandoff_android.data.request.BlockOrUnblockOtherUserRequest

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun blockOrUnblockOtherUser(blockOrUnblockOtherUserRequest: BlockOrUnblockOtherUserRequest): BlockOtherUserResponse {
        return userRemoteDataSource.blockOrUnblockOtherUser(blockOrUnblockOtherUserRequest)
    }

    override suspend fun getBlockedUserList(profileId: Int): List<BlockedUser> {
        return userRemoteDataSource.getBlockedUserList(profileId).result.map {
            it.toEntity()
        }
    }
}