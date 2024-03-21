package com.musify.app.domain.service

import com.musify.app.domain.models.CodeVerification
import com.musify.app.domain.models.Token
import com.musify.app.domain.models.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface UserService {


    @POST("api/login/")
    suspend fun login(@Body requestBody: RequestBody)

    @POST("api/otp-verify/")
    suspend fun verifyOtp(@Body body: CodeVerification): Token

    @GET("api/profile")
    suspend fun getProfileInfo(): User

    @POST("api/profile-update")
    suspend fun profileUpdate(@Body requestBody: RequestBody): User




}