package com.fotsapp.pushify.plugin

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson

class PushifyManager(private val bearerToken: String, private val projectId: String) {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun sendNotification(
        deviceToken: String,
        data: Map<String, String>? = null,
        title: String? = null,
        body: String? = null,
        callback: (Boolean, String) -> Unit
    ) {
        val payload = mapOf(
            "message" to     mapOf(
                "token" to deviceToken, "notification" to if (title != null && body != null) mapOf(
                    "title" to title, "body" to body
                ) else null, "data" to data
            )
        )

        val jsonBody = gson.toJson(payload)
        val requestBody = jsonBody.toRequestBody(jsonMediaType)

        val request =
            Request.Builder().url("https://fcm.googleapis.com/v1/projects/$projectId/messages:send")
                .header("Authorization", "Bearer $bearerToken").post(requestBody).build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: "{}"
                    callback(true, "Sent successfully! $responseBody")
                } else {
                    val errorBody = response.body?.string() ?: "Unknown error"
                    callback(false, "Error ${response.code}: $errorBody")
                }
            }
        } catch (e: Exception) {
            callback(false, "Exception: ${e.message}")
        }
    }
}

