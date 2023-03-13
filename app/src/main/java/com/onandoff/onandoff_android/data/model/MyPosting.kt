package com.onandoff.onandoff_android.data.model

data class MyPosting(
    val feedId : Int,
    val profileId : Int,
    val likeCount : String,
    val userId : Int,
    val personaId : Int,
    val profileImgUrl : String,
    val statusMessage : String,
    val profileName : String,
    val personaName : String,
    val content : String,
    val isLike : Int,
    val createdAt : String,
    val hashTagStr : String,
)
