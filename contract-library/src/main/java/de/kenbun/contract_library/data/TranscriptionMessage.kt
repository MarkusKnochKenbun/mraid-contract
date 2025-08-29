package de.kenbun.contract_library.data

import android.database.Cursor
import android.database.MatrixCursor

data class TranscriptionMessage(
    val sessionId: String,
    val text: String,
    val timestamp: Long,
) {

  fun toMatrixCursor(): MatrixCursor {
    val cursor = MatrixCursor(PROJECTION)

    cursor.addRow(arrayOf<Any>(this.sessionId, this.text, this.timestamp))

    return cursor
  }

  companion object {
    const val COLUMN_SESSION_ID = "sessionId"
    const val COLUMN_TEXT = "text"
    const val COLUMN_TIMESTAMP = "timestamp"

    val PROJECTION = arrayOf(COLUMN_SESSION_ID, COLUMN_TEXT, COLUMN_TIMESTAMP)

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
  }
}