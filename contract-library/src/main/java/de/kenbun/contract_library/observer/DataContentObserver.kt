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
import de.kenbun.contract_library.PackageContract.MRAID_PACKAGE_NAME
import de.kenbun.contract_library.data.InferenceServiceStatus
import de.kenbun.contract_library.data.TranscriptionMessage
import de.kenbun.contract_library.util.PackageUtil.isPackageInstalled

class DataContentObserver(
    private val context: Context,
    handler: Handler
) : ContentObserver(handler) {

    private var onNewTranscriptionMessage: ((String) -> Unit)? = null
    private var onNewStatus: ((String) -> Unit)? = null

    fun setTranscriptionCallback(callback: (String) -> Unit) {
        this.onNewTranscriptionMessage = callback
    }

    fun setStatusCallback(callback: (String) -> Unit) {
        this.onNewStatus = callback
    }


    fun clearAllCallbacks() {
        this.onNewTranscriptionMessage = null
        this.onNewStatus = null
    }

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
        onNewTranscriptionMessage?.let { callback ->
            context.contentResolver.query(CONTENT_URI_CURRENT_TRANSCRIPTION, null, null, null, null)
                ?.use { cursor ->
                    try {
                        callback(TranscriptionMessage.fromMatrixCursorAsJsonString(cursor))
                    } catch (e: Exception) {
                        Log.e("DataContentObserver", "Error processing transcription message", e)
                    }
                }
        } ?: Log.w("DataContentObserver", "Transcription callback not set, skipping fetch.")
    }

    private fun requestServiceStatus() {
        onNewStatus?.let { callback ->
            context.contentResolver.query(CONTENT_URI_SERVICE_STATUS, null, null, null, null)
                ?.use { cursor ->
                    try {
                        // Same here, callback is a Function1 for Java.
                        callback(InferenceServiceStatus.fromMatrixCursorAsString(cursor))
                    } catch (e: Exception) {
                        Log.e("DataContentObserver", "Error processing service status", e)
                    }
                }
        } ?: Log.w("DataContentObserver", "Status callback not set, skipping request.")
    }

    fun register(): Boolean {

        if (!isPackageInstalled(
                MRAID_PACKAGE_NAME, context.packageManager
            )
        ) {
            return false
        } else {
            context.contentResolver.registerContentObserver(
                CONTENT_URI_CURRENT_TRANSCRIPTION, true, this
            )

            context.contentResolver.registerContentObserver(
                CONTENT_URI_SERVICE_STATUS, true, this
            )
        }
        return true
    }

    fun unregister() {
        clearAllCallbacks()
        context.contentResolver.unregisterContentObserver(this)
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