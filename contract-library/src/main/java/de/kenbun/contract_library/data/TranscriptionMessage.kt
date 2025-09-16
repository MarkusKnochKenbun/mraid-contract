package de.kenbun.contract_library.data

import android.database.Cursor
import android.database.MatrixCursor
import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TranscriptionMessage(
    val sessionId: String,
    val text: String,
    val timestamp: Long,
) {

    fun toMatrixCursorAsJson(): MatrixCursor {
        return toMatrixCursorFromJsonString(Json.encodeToString(this))
    }

    companion object {

        const val COLUMN_JSON_DATA = "jsonData"
        val PROJECTION_JSON = arrayOf(COLUMN_JSON_DATA)

        fun toMatrixCursorFromJsonString(jsonString: String): MatrixCursor {

            val cursor = MatrixCursor(PROJECTION_JSON)
            cursor.addRow(arrayOf<Any>(jsonString))
            return cursor
        }

        fun fromMatrixCursor(cursor: Cursor): TranscriptionMessage? {

            jsonStringFromMatrixCursor(cursor)?.let { jsonString ->
                return try {
                    Json.decodeFromString<TranscriptionMessage>(jsonString)
                } catch (e: Exception) {
                    Log.e(
                        "TranscriptionMessage",
                        "Failed to deserialize message from cursor: ${e.message}"
                    )
                    null
                }
            }
            return null
        }

        fun jsonStringFromMatrixCursor(cursor: Cursor): String? {
            if (!cursor.moveToFirst()) {
                throw IllegalArgumentException("Cursor must contain at least one row for JSON extraction.")
            }
            val jsonIndex = cursor.getColumnIndexOrThrow(COLUMN_JSON_DATA)
            val jsonString = cursor.getString(jsonIndex)

            if (jsonString == null) {
                Log.e("TranscriptionMessage", "No serverMessage found in bundle")
                return null
            }

            return jsonString
        }

    }
}