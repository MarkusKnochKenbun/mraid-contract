package de.kenbun.contract_library.data

import android.database.Cursor
import android.database.MatrixCursor

enum class InferenceServiceStatus(val code: Int) {
    UNSET(0), TRANSCRIBING(1), STOPPED(2), STARTING(3), STOPPING(4);

    fun codeToMatrixCursor(): MatrixCursor {
        val matrixCursor = MatrixCursor(arrayOf(STATUS_CODE_COLUMN_NAME))
        matrixCursor.addRow(arrayOf(this.code))
        return matrixCursor
    }

    companion object {
        private const val STATUS_CODE_COLUMN_NAME = "statusCode"

        private val map = entries.associateBy(InferenceServiceStatus::code)

        fun fromCode(code: Int): InferenceServiceStatus? = map[code]

        fun codeFromCursor(cursor: Cursor): Int {

            if (!cursor.moveToFirst()) {
                throw IllegalArgumentException("Cursor must contain at least one row.")
            }

            val statusCodeColumnIndex = cursor.getColumnIndexOrThrow(STATUS_CODE_COLUMN_NAME)

            val statusCode = cursor.getInt(statusCodeColumnIndex)
            return statusCode
        }
    }
}