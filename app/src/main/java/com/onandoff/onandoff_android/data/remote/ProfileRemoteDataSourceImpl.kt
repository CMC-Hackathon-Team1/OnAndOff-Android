package com.onandoff.onandoff_android.data.remote

import com.onandoff.onandoff_android.data.api.user.MyPersonaInterface
import com.onandoff.onandoff_android.data.api.util.FormDataUtil
import com.onandoff.onandoff_android.data.model.CreateMyProfileResponse
import com.onandoff.onandoff_android.data.model.MyProfileListResponse
import com.onandoff.onandoff_android.data.model.MyProfileResponse
import com.onandoff.onandoff_android.data.model.error.NetworkError
import com.onandoff.onandoff_android.data.request.CreateProfileRequest

class ProfileRemoteDataSourceImpl(
    private val personaInterface: MyPersonaInterface
): ProfileRemoteDataSource {
    override suspend fun getMyPersona(profileId: Int): MyProfileResponse {
        return personaInterface.getMyPersona(profileId)
    }

    override suspend fun getMyPersonaList(): MyProfileListResponse {
        return personaInterface.getMyPersonaList()
    }

    override suspend fun createPersona(request: CreateProfileRequest): CreateMyProfileResponse {
        val formProfileName = FormDataUtil.getBody("profileName", request.profileName)       // 2-way binding 되어 있는 LiveData
        val formPersonaName = FormDataUtil.getBody("personaName", request.personaName)    // 2-way binding 되어 있는 LiveData
        val formStatusMessage = FormDataUtil.getBody("statusMessage", request.statusMessage)    // 2-way binding 되어 있는 LiveData
        var formImage = FormDataUtil.getImageBody("image", request.imagePath)
        if (request.imagePath.path.isEmpty()) {
            formImage = FormDataUtil.getBody("image", request.imagePath)
        }

        val createMyPersona = personaInterface.createMyPersona(
            formProfileName,
            formPersonaName,
            formStatusMessage,
            formImage
        )

        val error = NetworkError.create(errorCode = createMyPersona.statusCode, message = createMyPersona.message)

        if (error != null) {
            throw error
        }

        return createMyPersona
    }
}