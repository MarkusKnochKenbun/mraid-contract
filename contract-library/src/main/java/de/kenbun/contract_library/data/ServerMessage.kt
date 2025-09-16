package de.kenbun.contract_library.data

import android.os.Bundle
import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ServerMessage(
    val number: Int, val text: String, val timeStamp: Long
) {

    fun toBundle(): Bundle {
        val jsonString = Json.encodeToString(this)
        return toBundleFromJsonString(jsonString)
    }

    companion object {
        const val KEY = "serverMessage"

        fun toBundleFromJsonString(jsonString: String): Bundle {

            val bundle = Bundle().apply {
                putString(KEY, jsonString)
            }
            return bundle
        }

        fun fromBundle(bundle: Bundle): ServerMessage? {

            jsonStringFromBundle(bundle)?.let { jsonString ->

                return try {
                    Json.decodeFromString<ServerMessage>(jsonString)
                } catch (e: Exception) {
                    Log.e(
                        "ServerMessage", "Failed to deserialize message from bundle: ${e.message}"
                    )
                    null
                }
            }
            return null
        }

        fun jsonStringFromBundle(bundle: Bundle): String? {
            val jsonString = bundle.getString(KEY)
            if (jsonString == null) {
                Log.e("ServerMessage", "No serverMessage found in bundle")
                return null
            }
            return jsonString
        }
    }
}



