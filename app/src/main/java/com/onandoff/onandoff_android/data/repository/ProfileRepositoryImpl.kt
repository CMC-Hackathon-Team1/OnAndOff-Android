package com.onandoff.onandoff_android.data.repository

import com.onandoff.onandoff_android.data.model.CreateMyProfileResponse
import com.onandoff.onandoff_android.data.model.MyProfileListResponse
import com.onandoff.onandoff_android.data.model.MyProfileResponse
import com.onandoff.onandoff_android.data.remote.ProfileRemoteDataSource
import com.onandoff.onandoff_android.data.request.CreateProfileRequest

class ProfileRepositoryImpl(
    private val profileRemoteDataSource: ProfileRemoteDataSource
): ProfileRepository {
    override suspend fun getMyProfile(profileId: Int): MyProfileResponse {
        return profileRemoteDataSource.getMyPersona(profileId)
    }

    override suspend fun getMyProfileList(): MyProfileListResponse {
        return profileRemoteDataSource.getMyPersonaList()
    }

    override suspend fun createProfile(request: CreateProfileRequest): CreateMyProfileResponse {
        return profileRemoteDataSource.createPersona(request)
    }
}