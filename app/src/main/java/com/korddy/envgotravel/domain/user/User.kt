package com.korddy.envgotravel.domain.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class User(
    @SerializedName("id") val id: UUID,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("birthdate") val birthdate: String? = null,
    @SerializedName("is_driver") val isDriver: Boolean = false,
    @SerializedName("is_available") val isAvailable: Boolean = false,
    @SerializedName("is_superuser") val isSuperuser: Boolean = false,
    @SerializedName("is_staff") val isStaff: Boolean = false,
    @SerializedName("is_active") val isActive: Boolean = true,
    @SerializedName("wallet_balance") val walletBalance: Double = 0.0,
    @SerializedName("profile_picture") val profilePicture: String? = null, // ALTERADO
    @SerializedName("last_login") val lastLogin: String? = null,
    @SerializedName("date_joined") val dateJoined: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
) : Serializable
