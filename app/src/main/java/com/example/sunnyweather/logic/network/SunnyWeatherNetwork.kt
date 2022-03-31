package com.example.sunnyweather.logic.network

import android.app.DownloadManager
import android.telecom.Call
import retrofit2.Response
import retrofit2.await
import javax.security.auth.callback.Callback
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()
        suspend fun <T> retrofit2.Call<T>.await(): T {
            return suspendCoroutine { continuation ->
                enqueue(object : retrofit2.Callback<T> {
                    override fun onResponse(call: retrofit2.Call<T>, response: Response<T>) {
                        val body = response.body()
                        if (body != null) continuation.resume(body)
                        else continuation.resumeWithException(RuntimeException("response body is null"))
                    }

                    override fun onFailure(call: retrofit2.Call<T>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }
        }

    }