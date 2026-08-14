package com.ashish.signal.data.network

import com.ashish.signal.data.model.ActionResponseDto
import com.ashish.signal.data.model.ScreenResponseDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/** Retrofit interface for the Signal BFF. Routes match bff/src/screens and bff/src/actions. */
interface BffApi {

    @GET("screen/{screenId}")
    suspend fun getScreen(@Path("screenId") screenId: String): ScreenResponseDto

    @POST("action/{action}")
    suspend fun postAction(
        @Path("action") action: String,
        @Body params: Map<String, @JvmSuppressWildcards Any>
    ): ActionResponseDto
}

object BffApiClient {
    // 10.0.2.2 is the emulator's alias for the host machine's localhost.
    // Swap for a real BFF host when building against a device or a deployed backend.
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val api: BffApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BffApi::class.java)
    }
}
