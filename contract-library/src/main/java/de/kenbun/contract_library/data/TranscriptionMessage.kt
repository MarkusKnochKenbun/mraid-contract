package de.kenbun.contract_library.data

import android.database.Cursor
import android.database.MatrixCursor
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TranscriptionMessage(
    val sessionId: String,
    val text: String,
    val timestamp: Long,
) {

  fun toJsonString(): String {
    return Json.encodeToString(this)
  }

  fun toMatrixCursorWithFields(): MatrixCursor {
    val cursor = MatrixCursor(PROJECTION_FIELDS)
    cursor.addRow(arrayOf<Any>(this.sessionId, this.text, this.timestamp))
    return cursor
  }

  fun toMatrixCursorWithJson(): MatrixCursor {
    val cursor = MatrixCursor(PROJECTION_JSON)
    cursor.addRow(arrayOf<Any>(this.toJsonString()))
    return cursor
  }

  companion object {
    const val COLUMN_SESSION_ID = "sessionId"
    const val COLUMN_TEXT = "text"
    const val COLUMN_TIMESTAMP = "timestamp"

    val PROJECTION_FIELDS = arrayOf(COLUMN_SESSION_ID, COLUMN_TEXT, COLUMN_TIMESTAMP)

    fun fromMatrixCursor(cursor: Cursor): TranscriptionMessage {
      // Move to the first row to read the data
      if (!cursor.moveToFirst()) {
        throw IllegalArgumentException("Cursor must contain at least one row.")
      }

      val sessionIdIndex = cursor.getColumnIndexOrThrow(COLUMN_SESSION_ID)
      val textIndex = cursor.getColumnIndexOrThrow(COLUMN_TEXT)
      val timestampIndex = cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)

      val sessionId = cursor.getString(sessionIdIndex)
      val text = cursor.getString(textIndex)
      val timestamp = cursor.getLong(timestampIndex)

      return TranscriptionMessage(sessionId, text, timestamp)
    }

    const val COLUMN_JSON_DATA = "jsonData"
    val PROJECTION_JSON = arrayOf(COLUMN_JSON_DATA)

    fun fromMatrixCursorAsJsonString(cursor: Cursor): String {
      if (!cursor.moveToFirst()) {
        throw IllegalArgumentException("Cursor must contain at least one row for JSON extraction.")
      }
      val jsonIndex = cursor.getColumnIndexOrThrow(COLUMN_JSON_DATA)
      val jsonString = cursor.getString(jsonIndex)
      return jsonString
    }

    fun fromJsonString(jsonString: String): TranscriptionMessage {
      return Json.decodeFromString(jsonString)
    }
  }
}