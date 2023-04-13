package com.onandoff.onandoff_android.data.model.error

sealed class NetworkError(message: String) : Throwable(message = message) {

    class BodyError(message: String) : NetworkError(message)
    class JwtError(message: String) : NetworkError(message)
    class DBError(message: String) : NetworkError(message)
    class ServerError(message: String) : NetworkError(message)
    class LimitProfileCountError(message: String) : NetworkError(message)
    class AlreadyExistProfileError(message: String) : NetworkError(message)
    class NoProfileError(message: String) : NetworkError(message)
    class NotMyProfileError(message: String) : NetworkError(message)
    class NoProfileIdError(message: String) : NetworkError(message)
    class JwtTokenAndProfileNotSame(message: String) : NetworkError(message)
    class InvalidFromProfileId(message: String) : NetworkError(message)
    class InvalidToProfileId(message: String) : NetworkError(message)
    class InvalidFeedDataError(message: String) : NetworkError(message)
    class AlreadyReportedFeedError(message: String) : NetworkError(message)
    class InvalidReportCategoryIdError(message: String) : NetworkError(message)
    class NoReportReasonError(message: String) : NetworkError(message)


    companion object {
        fun create(errorCode: Int, message: String): NetworkError? =
            when (errorCode) {
                400 -> BodyError(message)
                401 -> JwtError(message)
                500 -> DBError(message)
                501 -> ServerError(message)
                1500 -> LimitProfileCountError(message)
                1501 -> AlreadyExistProfileError(message)
                1503 -> NoProfileError(message)
                1504 -> NotMyProfileError(message)
                1505 -> NoProfileIdError(message)
                1506 -> JwtTokenAndProfileNotSame(message)
                2103 -> InvalidFromProfileId(message)
                2104 -> InvalidToProfileId(message)
                2200 -> InvalidFeedDataError(message)
                3000 -> AlreadyReportedFeedError(message)
                3001 -> InvalidReportCategoryIdError(message)
                3002 -> NoReportReasonError(message)
                3701 -> InvalidFromProfileId(message)
                3702 -> InvalidToProfileId(message)
                else -> null
            }
    }
}


