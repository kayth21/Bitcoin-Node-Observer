package com.ceaver.bno.network

data class Response<T> private constructor(
    val result: T?,
    val error: String?
) {
    companion object {
        fun <T> success(result: T): Response<T> {
            return Response(result, null)
        }

        fun <T> error(error: String): Response<T> {
            return Response(null, error)
        }
    }

    fun isSuccessful(): Boolean {
        return result != null
    }
}

