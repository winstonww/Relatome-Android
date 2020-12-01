package com.example.relatome.network

import com.example.relatome.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface RelatomeApiService {
    @POST("user/login")
    suspend fun login(@Body body: LoginRequest) : LoginResponse

    @GET("relationship/list")
    suspend fun getRelationships(@Header("auth-token") authToken: String): List<RelationshipResponse>
}

object RelatomeApi {
    val retrofitService : RelatomeApiService by lazy {
        retrofit.create(RelatomeApiService::class.java)
    }
}