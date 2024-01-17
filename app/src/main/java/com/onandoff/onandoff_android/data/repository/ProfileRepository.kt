package com.onandoff.onandoff_android.data.repository

import com.onandoff.onandoff_android.data.model.CreateMyProfileResponse
import com.onandoff.onandoff_android.data.model.MyProfileItem
import com.onandoff.onandoff_android.data.model.MyProfileListResponse
import com.onandoff.onandoff_android.data.model.MyProfileResponse
import com.onandoff.onandoff_android.data.request.CreateProfileRequest

interface ProfileRepository {
    suspend fun getMyProfile(profileId: Int): MyProfileItem

    suspend fun getMyProfileList(): MyProfileListResponse

    suspend fun createProfile(request: CreateProfileRequest): CreateMyProfileResponse
}
