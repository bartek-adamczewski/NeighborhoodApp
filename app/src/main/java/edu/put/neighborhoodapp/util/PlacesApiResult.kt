package edu.put.neighborhoodapp.util

import retrofit2.HttpException
import retrofit2.Response

sealed class PlacesApiResult<out D> {
    data class Success<out D>(val data: D) : PlacesApiResult<D>()
    data class Exception<out D>(val error: PlacesApiError) : PlacesApiResult<D>()
}

sealed class PlacesApiError {
    data class Http(val code: Int, val msg: String, val throwable: Throwable) : PlacesApiError()
    object Accepted : PlacesApiError()
    data class Unknown(val msg: String?, val throwable: Throwable) : PlacesApiError()
}

suspend fun <D> wrapApiCall(
    apiCall: suspend () -> Response<D>
): PlacesApiResult<D> = try {
    val response = apiCall()
    if (response.code() == 202) {
        throw AcceptedResponse()
    }
    PlacesApiResult.Success(data = response.body() ?: throw Exception("No body"))
} catch (t: Throwable) {
    PlacesApiResult.Exception(
        error = when (t) {
            is HttpException -> PlacesApiError.Http(t.code(), t.message(), t)
            is AcceptedResponse -> PlacesApiError.Accepted
            else -> PlacesApiError.Unknown(t.message, t)
        }
    )
}

class AcceptedResponse: Exception()