package nl.marc_apps.ovgo.test_utils

import io.mockk.every
import io.mockk.mockk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> mockCallWithResponse(response: Response<T>) = mockk<Call<T>> {
    val call = this
    every { enqueue(any()) } answers {
        arg<Callback<T>>(0).onResponse(call, response)
    }
}
