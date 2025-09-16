package de.kenbun.contract_library.resolver

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import de.kenbun.contract_library.DataContentProviderContract.DATA_PROVIDER_AUTHORITY
import de.kenbun.contract_library.DataContentProviderContract.REQUEST_SERVICE_STATUS_PATH
import de.kenbun.contract_library.data.ContentProviderMethod
import de.kenbun.contract_library.data.InferenceServiceStatus
import de.kenbun.contract_library.data.ServerMessage

class DataContentResolver(private val contentResolver: ContentResolver) {

    fun requestServiceStatus(): Int? {

        Log.i("DataContentResolver", "requestServiceStatus")

        val uri = Uri.Builder().scheme("content").authority(DATA_PROVIDER_AUTHORITY)
            .appendPath(REQUEST_SERVICE_STATUS_PATH).build()

        return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                InferenceServiceStatus.codeFromCursor(cursor)
            } else {
                null
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun sendAndReceiveData(message: String): String? {
        Log.i("DataContentResolver", "sendAndReceiveData")

        val responseBundle = contentResolver.call(
            DATA_PROVIDER_AUTHORITY,
            ContentProviderMethod.SendAndReceiveData.name,
            null,
            ServerMessage.toBundleFromJsonString(message)
        )

        if (responseBundle == null) {
            Log.i("DataContentResolver", "responseBundle was null")
            return null
        }

        val jsonStringServerResponse = ServerMessage.jsonStringFromBundle(responseBundle)

        Log.i("DataContentResolver", "jsonStringServerResponse: $jsonStringServerResponse")

        return jsonStringServerResponse
    }


}

