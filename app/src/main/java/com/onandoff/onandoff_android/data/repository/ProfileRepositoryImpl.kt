package com.onandoff.onandoff_android.data.repository

import com.onandoff.onandoff_android.data.model.CreateMyProfileResponse
import com.onandoff.onandoff_android.data.model.MyProfileListResponse
import com.onandoff.onandoff_android.data.remote.ProfileRemoteDataSource
import com.onandoff.onandoff_android.data.request.CreateProfileRequest
import com.onandoff.onandoff_android.presentation.home.viewmodel.MyProfileItem

class ProfileRepositoryImpl(
    private val profileRemoteDataSource: ProfileRemoteDataSource
): ProfileRepository {
    override suspend fun getMyProfile(profileId: Int): MyProfileItem {
        return MyProfileItem(
            myProfile = profileRemoteDataSource.getMyPersona(profileId),
            isSelected = true
        )
    }

    override suspend fun getMyProfileList(): MyProfileListResponse {
        return profileRemoteDataSource.getMyPersonaList()
    }

    override suspend fun createProfile(request: CreateProfileRequest): CreateMyProfileResponse {
        return profileRemoteDataSource.createPersona(request)
    }
}

