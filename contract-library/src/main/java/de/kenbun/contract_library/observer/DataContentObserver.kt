package de.kenbun.contract_library.observer

import android.content.Context
import android.content.UriMatcher
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.util.Log
import de.kenbun.contract_library.DataContentProviderContract.CONTENT_URI_CURRENT_TRANSCRIPTION
import de.kenbun.contract_library.DataContentProviderContract.CONTENT_URI_SERVICE_STATUS
import de.kenbun.contract_library.DataContentProviderContract.CURRENT_TRANSCRIPTION_CODE
import de.kenbun.contract_library.DataContentProviderContract.CURRENT_TRANSCRIPTION_PATH
import de.kenbun.contract_library.DataContentProviderContract.DATA_PROVIDER_AUTHORITY
import de.kenbun.contract_library.DataContentProviderContract.REQUEST_SERVICE_STATUS_CODE
import de.kenbun.contract_library.DataContentProviderContract.REQUEST_SERVICE_STATUS_PATH
import de.kenbun.contract_library.data.InferenceServiceStatus
import de.kenbun.contract_library.data.TranscriptionMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DataContentObserver(private val context: Context, handler: Handler) :
    ContentObserver(handler) {

    private val _transcription = MutableStateFlow<TranscriptionMessage?>(null)
    val transcription = _transcription.asStateFlow()

    private val _inferenceServiceStatus =
        MutableStateFlow(InferenceServiceStatus.UNSET)
    val inferenceServiceStatus = _inferenceServiceStatus.asStateFlow()

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        Log.d("DataContentObserver", "Data has changed at: $uri")

        when (matcher.match(uri)) {

            CURRENT_TRANSCRIPTION_CODE -> {
                fetchCurrentTranscription()
            }

            REQUEST_SERVICE_STATUS_CODE -> {
                requestServiceStatus()
            }

        }

    }

    private fun fetchCurrentTranscription() {
        context.contentResolver.query(CONTENT_URI_CURRENT_TRANSCRIPTION, null, null, null, null)
            ?.use { cursor ->
                _transcription.value = TranscriptionMessage.fromMatrixCursor(cursor)
            }
    }

    private fun requestServiceStatus() {
        context.contentResolver.query(CONTENT_URI_SERVICE_STATUS, null, null, null, null)
            ?.use { cursor ->
                _inferenceServiceStatus.value = InferenceServiceStatus.fromMatrixCursor(cursor)
            }
    }

    companion object {
        private val matcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(DATA_PROVIDER_AUTHORITY, CURRENT_TRANSCRIPTION_PATH, CURRENT_TRANSCRIPTION_CODE)
            addURI(
                DATA_PROVIDER_AUTHORITY, REQUEST_SERVICE_STATUS_PATH, REQUEST_SERVICE_STATUS_CODE
            )
        }

    }

}