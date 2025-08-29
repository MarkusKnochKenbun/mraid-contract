package de.kenbun.contract_library

import android.database.Cursor
import android.database.MatrixCursor

enum class InferenceServiceStatus {
  TRANSCRIBING,
  STOPPED,
  UNSET,
  STARTING,
  STOPPING;

  companion object {
    private const val COLUMN_NAME = "status"

    fun fromMatrixCursor(cursor: Cursor): InferenceServiceStatus {

      if (!cursor.moveToFirst()) {
        throw IllegalArgumentException("Cursor must contain at least one row.")
      }

      val statusIndex = cursor.getColumnIndex(COLUMN_NAME)
      val statusString = cursor.getString(statusIndex)
      return InferenceServiceStatus.valueOf(statusString)
    }
  }

  fun toMatrixCursor(): MatrixCursor {
    val matrixCursor = MatrixCursor(arrayOf(COLUMN_NAME))
    matrixCursor.addRow(arrayOf(this.name))
    return matrixCursor
  }
}
