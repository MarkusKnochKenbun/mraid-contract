package de.kenbun.contract_library.data

import android.os.Bundle
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ServerMessage(
    val number: Int, val text: String, val timeStamp: Long
) {

    fun toBundle(): Bundle {
        val jsonString = Json.encodeToString(this)
        return Bundle().apply {
            putString(KEY, jsonString)
        }
    }

    companion object {
        const val KEY = "serverMessage"

        fun fromBundle(bundle: Bundle): ServerMessage? {
            val jsonString = bundle.getString(KEY)
            return if (jsonString != null) {
                Json.decodeFromString<ServerMessage>(jsonString)
            } else {
                null
            }
        }
    }
}
