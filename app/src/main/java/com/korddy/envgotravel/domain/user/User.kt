package com.korddy.envgotravel.domain.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class User(
    @SerializedName("id") val id: UUID,
    @SerializedName("username") val username: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("nickname") val nickname: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("birthdate") val birthdate: String? = null,
    @SerializedName("is_driver") val isDriver: Boolean = false,
    @SerializedName("is_available") val isAvailable: Boolean = true,
    @SerializedName("profile_picture") val profilePicture: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("wallet_balance") val walletBalance: Double = 0.0,
    @SerializedName("two_factor_enabled") val twoFactorEnabled: Boolean = false,
    @SerializedName("two_factor_secret") val twoFactorSecret: String? = null
) : Serializable