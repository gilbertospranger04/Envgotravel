package com.korddy.envgotravel.services.session

import android.content.Context
import java.io.File
import org.json.JSONObject

object SessionCache {

    private const val TOKEN_FILE = "session_cache.json"
    private const val USER_FILE = "session_user.json"
    private const val EXPIRATION_DAYS = 2
    private const val MILLIS_PER_DAY = 24 * 60 * 60 * 1000L

    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // ======================
    // TOKEN MANAGEMENT
    // ======================

    fun setAuthToken(token: String) = saveAuthToken(token)

    fun saveAuthToken(token: String) {
        val now = System.currentTimeMillis()
        val expiresAt = now + (EXPIRATION_DAYS * MILLIS_PER_DAY)
        val json = JSONObject()
            .put("token", token)
            .put("created_at", now)
            .put("expires_at", expiresAt)

        appContext?.let {
            val file = File(it.cacheDir, TOKEN_FILE)
            file.writeText(json.toString())
        }
    }

    fun getAuthToken(): String? {
        appContext ?: return null
        val file = File(appContext!!.cacheDir, TOKEN_FILE)
        if (!file.exists()) return null

        return try {
            val json = JSONObject(file.readText())
            val expiresAt = json.optLong("expires_at", 0)
            if (System.currentTimeMillis() > expiresAt) {
                clearAuthToken()
                null
            } else {
                json.optString("token", null)
            }
        } catch (_: Exception) {
            clearAuthToken()
            null
        }
    }

    fun clearAuthToken() {
        appContext?.let {
            val file = File(it.cacheDir, TOKEN_FILE)
            if (file.exists()) file.delete()
        }
    }

    // ======================
    // USER MANAGEMENT
    // ======================

    fun saveCurrentUser(json: String) {
        appContext?.let {
            File(it.cacheDir, USER_FILE).writeText(json)
        }
    }

    fun getCurrentUser(): String? {
        appContext ?: return null
        val file = File(appContext!!.cacheDir, USER_FILE)
        if (!file.exists()) return null
        return try {
            file.readText()
        } catch (_: Exception) {
            null
        }
    }

    fun clearCurrentUser() {
        appContext?.let {
            val file = File(it.cacheDir, USER_FILE)
            if (file.exists()) file.delete()
        }
    }

    fun clearAll() {
        clearAuthToken()
        clearCurrentUser()
    }
}