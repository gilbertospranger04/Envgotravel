package com.korddy.envgotravel.services.api

import com.google.gson.annotations.SerializedName
import com.korddy.envgotravel.domain.user.User
import java.io.Serializable

// =====================
// AUTHENTICAÇÃO
// =====================

data class SignupRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("confirm_password") val confirmPassword: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("birthdate") val birthdate: String? = null,
    @SerializedName("is_driver") val isDriver: Boolean = false,
    @SerializedName("profile_picture") val profilePicture: String? = null
) : Serializable

data class SigninRequest(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("password") val password: String
)

data class SigninResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: User
)

// OTP
data class SendOtpRequest(@SerializedName("phone_number") val phoneNumber: String)
data class SendOtpResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String?
)
data class VerifyOtpRequest(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("otp") val otp: String
)
data class VerifyOtpResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("phone_number") val phoneNumber: String?
)
data class CheckPhoneResponse(
    @SerializedName("exists") val exists: Boolean,
    @SerializedName("phone_number") val phoneNumber: String
)

// =====================
// PERFIL / USUÁRIOS
// =====================

data class UserUpdateRequest(
    @SerializedName("username") val username: String?,
    @SerializedName("password") val password: String? = null,
    @SerializedName("confirm_password") val confirmPassword: String? = null,
    @SerializedName("email") val email: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("birthdate") val birthdate: String?,
    @SerializedName("is_driver") val isDriver: Boolean? = null,
    @SerializedName("profile_picture") val profilePicture: String? = null,
    @SerializedName("wallet_balance") val walletBalance: Double? = null
)

data class ApiMessageResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("detail") val detail: String? = null
)

// =====================
// AVATARES
// =====================

data class AvatarResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("avatar_url") val avatarUrl: String
)

// =====================
// TWO-FACTOR AUTH (2FA) - ATUALIZADO PARA NOVO BACKEND
// =====================

data class Setup2FAResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("secret_key") val secretKey: String,
    @SerializedName("qr_code_base64") val qrCodeBase64: String,
    @SerializedName("otp_uri") val otpUri: String,
    @SerializedName("message") val message: String
)

data class Verify2FASetupRequest(@SerializedName("code") val code: String)
data class Verify2FASetupResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("verified") val verified: Boolean,
    @SerializedName("message") val message: String
)

data class Verify2FALoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("code") val code: String
)

data class Verify2FALoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("verified") val verified: Boolean,
    @SerializedName("token") val token: String?,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("user") val user: User?,
    @SerializedName("message") val message: String
)

data class Disable2FARequest(@SerializedName("password") val password: String)

data class Signin2FARequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("code") val code: String? = null
)

data class Signin2FAResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("token") val token: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user") val user: User,
    @SerializedName("two_factor_enabled") val twoFactorEnabled: Boolean,
    @SerializedName("requires_2fa") val requires2fa: Boolean = false
)