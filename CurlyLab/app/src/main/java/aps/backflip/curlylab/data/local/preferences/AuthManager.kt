package aps.backflip.curlylab.data.local.preferences

import android.content.Context
import aps.backflip.curlylab.data.remote.model.response.auth.AuthResponse

object AuthManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"

    fun saveAuthData(context: Context, response: AuthResponse) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, response.accessToken)
            .putString(KEY_REFRESH_TOKEN, response.refreshToken)
            .putString(KEY_USER_ID, response.userId)
            .putString(KEY_USERNAME, response.username)
            .apply()
    }

    fun getAccessToken(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_REFRESH_TOKEN, null)

    fun getUserId(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_ID, null)

    fun getUsername(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, null)

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_USER_ID)
            .remove(KEY_USERNAME)
            .apply()
    }
}