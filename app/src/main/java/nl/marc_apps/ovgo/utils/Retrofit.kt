package nl.marc_apps.ovgo.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.*

inline fun <reified T> retrofit(block: Retrofit.Builder.() -> Unit): T {
    return Retrofit.Builder().also(block).build().create()
}

inline fun httpClient(block: OkHttpClient.Builder.() -> Unit): OkHttpClient {
    return OkHttpClient.Builder().also(block).build()
}

suspend fun <T : Any> Call<T>.awaitResult(): ApiResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            ApiResult.Success(this@awaitResult.await())
        } catch (error: KotlinNullPointerException) {
            ApiResult.Failure(ApiError.BodyWasNull)
        } catch (error: HttpException) {
            ApiResult.Failure(ApiError.HttpError(error))
        } catch (error: Throwable) {
            ApiResult.Failure(ApiError.Exception(error))
        }
    }
}

sealed class ApiError(val error: Throwable) {
    object BodyWasNull : ApiError(KotlinNullPointerException())
    class HttpError(error: HttpException) : ApiError(error)
    class Exception(error: Throwable) : ApiError(error)
}

sealed class ApiResult<out T> {
    data class Success<out T>(val body: T): ApiResult<T>()
    data class Failure(val apiError: ApiError): ApiResult<Nothing>()

    val bodyOrNull: T?
        get() = if (this is Success) body else null

    inline fun onFailure(block: (ApiError) -> Unit): ApiResult<T> {
        if (this is Failure) {
            block(apiError)
        }

        return this
    }

    inline fun onSuccess(block: (T) -> Unit): ApiResult<T> {
        if (this is Success) {
            block(this.body)
        }

        return this
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <R> mapSuccess(block: (T) -> R): ApiResult<R> {
        return when(this) {
            is Success -> Success(block(this.body))
            else -> this as ApiResult<R>
        }
    }
}
