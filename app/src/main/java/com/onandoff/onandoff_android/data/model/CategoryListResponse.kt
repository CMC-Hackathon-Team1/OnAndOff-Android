package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class CategoryListResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("result")
    val result: List<CategoryResponse>?
)

data class CategoryResponse(
    @SerializedName("categoryId")
    val categoryId: Int,
    @SerializedName("categoryName")
    val categoryName: String?,
    val isInvalid: Boolean = false,
)
