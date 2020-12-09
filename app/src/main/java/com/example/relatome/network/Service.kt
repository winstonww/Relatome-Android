package com.example.relatome.network

import com.example.relatome.utils.Constants

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface RelatomeApiService {
    @POST("user/login")
    suspend fun login(@Body body: LoginRequest) : LoginResponse

    @GET("relationship/list")
    suspend fun getRelationships(@Header("auth-token") authToken: String): List<RelationshipResponse>

    @POST("relationship/add")
    suspend fun addRelationship(
        @Header("auth-token") authToken: String,
        @Body body: AddRelationshipRequest): AddRelationshipResponse

    @HTTP(method = "DELETE", path = "relationship/delete", hasBody = true)
    suspend fun deleteRelationship(
        @Header("auth-token") authToken: String,
        @Body body: DeleteRelationshipRequest): DeleteRelationshipResponse

    @GET("relationship/as/name")
    suspend fun getAs(@Header("auth-token") authToken: String, @Query("name") pattern: String): List<AsResponse>

    @GET("relationship/pending/user")
    suspend fun getPendingRelationships(
        @Header("auth-token") authToken: String
    ): List<PendingRelationshipResponse>

    @POST("relationship/respond")
    suspend fun fillRelationship(@Header("auth-token") authToken: String, @Body body: FillRelationshipRequest): ResponseBody

    @GET("relationship/response")
    suspend fun getRelationshipResponses(@Header("auth-token") authToken: String): List<RelationshipResponseResponse>
}

object RelatomeApi {
    val retrofitService : RelatomeApiService by lazy {
        retrofit.create(RelatomeApiService::class.java)
    }
}