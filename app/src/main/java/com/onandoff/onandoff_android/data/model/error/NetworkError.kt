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
    class JwtTokenAndProfileNotSame(message: String) : NetworkError(message)


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
                1506 -> JwtTokenAndProfileNotSame(message)
                else -> null
            }
    }
}


