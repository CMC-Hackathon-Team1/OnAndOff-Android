package com.onandoff.onandoff_android.data.remote

import com.onandoff.onandoff_android.data.model.CreateMyProfileResponse
import com.onandoff.onandoff_android.data.model.MyProfileListResponse
import com.onandoff.onandoff_android.data.model.MyProfileResponse
import com.onandoff.onandoff_android.data.request.CreateProfileRequest

interface ProfileRemoteDataSource {
    suspend fun getMyPersona(profileId: Int): MyProfileResponse

    suspend fun getMyPersonaList(): MyProfileListResponse

    suspend fun createPersona(request: CreateProfileRequest): CreateMyProfileResponse
}
