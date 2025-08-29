package de.kenbun.contract_library.resolver

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import de.kenbun.contract_library.DataContentProviderContract.DATA_PROVIDER_AUTHORITY
import de.kenbun.contract_library.DataContentProviderContract.REQUEST_SERVICE_STATUS_PATH
import de.kenbun.contract_library.data.ContentProviderMethod
import de.kenbun.contract_library.data.InferenceServiceStatus
import de.kenbun.contract_library.data.ServerMessage
import de.kenbun.contract_library.util.AndroidVersionUtil.isEqualOrHigher
import javax.inject.Inject

class DataContentResolver
@Inject constructor(private val contentResolver: ContentResolver) {

    fun requestServiceStatus(): InferenceServiceStatus? {

        Log.i("DataContentResolver", "requestServiceStatus")

        val uri = Uri.Builder().scheme("content").authority(DATA_PROVIDER_AUTHORITY)
            .appendPath(REQUEST_SERVICE_STATUS_PATH).build()

        return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                InferenceServiceStatus.fromMatrixCursor(cursor)
            } else {
                null
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun sendAndReceiveData(message: ServerMessage): ServerMessage? {
        Log.i("DataContentResolver", "sendAndReceiveData")

        val requestBundle = Bundle().apply {
            putParcelable(ServerMessage.KEY, message)
        }

        val responseBundle = contentResolver.call(
            DATA_PROVIDER_AUTHORITY,
            ContentProviderMethod.SendAndReceiveData.name,
            null,
            requestBundle
        )

        if (responseBundle == null) {
            Log.i("DataContentResolver", "responseBundle was null")
            return null
        }

        val serverResponse: ServerMessage? = if (isEqualOrHigher(Build.VERSION_CODES.TIRAMISU)) {
            responseBundle.getParcelable(ServerMessage.KEY, ServerMessage::class.java)
        } else {
            responseBundle.getParcelable(ServerMessage.KEY)
        }

        Log.i("DataContentResolver", "serverResponse: $serverResponse")

        return serverResponse
    }


}

