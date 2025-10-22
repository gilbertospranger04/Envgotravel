package com.korddy.envgotravel.services.api

import com.korddy.envgotravel.domain.map.RouteResponse
import com.korddy.envgotravel.domain.user.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ===== AUTH =====
    @POST("auth/signup/")
    suspend fun signup(@Body request: SignupRequest): Response<User>

    @POST("auth/signin/")
    suspend fun signinPw(@Body request: SigninRequest): Response<SigninResponse>

    @POST("auth/signin/") // manter compatibilidade se houver chamadas antigas
    suspend fun signin(@Body request: SigninRequest): Response<SigninResponse>

    @POST("auth/send-otp/")
    suspend fun sendOtp(@Body body: SendOtpRequest): Response<SendOtpResponse>

    @POST("auth/verify-otp/")
    suspend fun verifyOtp(@Body body: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("auth/verify-code/") // manter compatibilidade
    suspend fun verifyCode(@Body body: VerifyOtpRequest): Response<VerifyOtpResponse>

    @GET("auth/check-phone/")
    suspend fun checkPhone(@Query("phone_number") phoneNumber: String): Response<CheckPhoneResponse>

    // ===== PROFILE =====
    @GET("profile/")
    suspend fun profileMe(): Response<User>

    @GET("profile/") // compatibilidade
    suspend fun getCurrentUser(): Response<User>

    @PUT("profile/")
    suspend fun updateMyProfile(@Body request: UserUpdateRequest): Response<User>

    @DELETE("profile/")
    suspend fun deleteMyAccount(): Response<ApiMessageResponse>

    // ===== AVATAR =====
    @GET("avatars/{user_id}/")
    suspend fun getAvatar(@Path("user_id") userId: String): Response<AvatarResponse>

    @Multipart
    @POST("profile/avatar/")
    suspend fun uploadAvatar(@Part avatar: MultipartBody.Part): Response<User>

    @Multipart
    @PUT("avatars/{user_id}/")
    suspend fun updateAvatar(
        @Path("user_id") userId: String,
        @Part avatar: MultipartBody.Part
    ): Response<User>

    @DELETE("avatars/{user_id}/")
    suspend fun deleteAvatar(@Path("user_id") userId: String): Response<ApiMessageResponse>

    // ===== 2FA =====
    @GET("auth/2fa/setup/")
    suspend fun setup2FA(): Response<Setup2FAResponse>

    @POST("auth/2fa/verify-setup/")
    suspend fun verify2FASetup(@Body request: Verify2FASetupRequest): Response<Verify2FASetupResponse>

    @POST("auth/2fa/verify-login/")
    suspend fun verify2FALogin(@Body request: Verify2FALoginRequest): Response<Verify2FALoginResponse>

    @POST("auth/2fa/disable/")
    suspend fun disable2FA(@Body request: Disable2FARequest): Response<ApiMessageResponse>

    @POST("auth/signin-2fa/")
    suspend fun signinWith2FA(@Body request: Signin2FARequest): Response<Signin2FAResponse>

    // ===== USERS =====
    @GET("users/")
    suspend fun getUsers(): Response<List<User>>

    @DELETE("users/{id}/")
    suspend fun deleteUser(@Path("id") userId: String): Response<ApiMessageResponse>

    // ===== ROUTE (MAP) =====
    @GET("route/")
    suspend fun getRoute(
        @Query("from") from: String,
        @Query("to") to: String
    ): Response<RouteResponse>
}

// ===== Funções de extensão úteis =====
suspend fun ApiService.getMyProfile(): User? {
    // Tenta profileMe() primeiro, depois getCurrentUser() se necessário
    val response: Response<User> = try {
        this.profileMe()
    } catch (e: Exception) {
        this.getCurrentUser()
    }
    return if (response.isSuccessful) response.body() else null
}