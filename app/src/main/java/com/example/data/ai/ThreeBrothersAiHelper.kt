package com.example.data.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object ThreeBrothersAiHelper {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    suspend fun getAiRecommendation(userQuery: String, catalogSummary: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Welcome to Three Brothers! Based on Mrs. Farhana Nadeem's curated collection, we recommend checking out our **Royal Gold Chronograph Watch** ($249.99) and **Empress Silk Embroidery Kurti** ($89.99) which match your style preference!"
        }

        try {
            val systemPrompt = """
                You are the official AI Personal Shopping Concierge for Three Brothers, a luxury e-commerce platform owned by Mrs. Farhana Nadeem.
                Assist customers warmly and professionally.
                Available Products Summary:
                $catalogSummary
                
                Respond in clean, friendly markdown text with top 2 recommendations and why they match the user's intent.
            """.trimIndent()

            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", "$systemPrompt\n\nUser Question: $userQuery"))
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseText = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val responseJson = JSONObject(responseText)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val content = candidates.getJSONObject(0).optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "Our AI Assistant recommends browsing our Featured Royal Collection!")
                    }
                }
            }
            "Our Mrs. Farhana Nadeem Collection offers luxury watches, kurtis, and fragrances tailored for your needs."
        } catch (e: Exception) {
            "Here are top picks from Three Brothers: Royal Gold Chronograph Watch ($249.99) & Empress Silk Embroidery Kurti ($89.99)."
        }
    }
}
