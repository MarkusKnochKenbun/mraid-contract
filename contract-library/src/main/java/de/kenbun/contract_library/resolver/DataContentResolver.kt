package de.kenbun.contract_library.resolver

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import de.kenbun.contract_library.DataContentProviderContract.DATA_PROVIDER_AUTHORITY
import de.kenbun.contract_library.DataContentProviderContract.REQUEST_SERVICE_STATUS_PATH
import de.kenbun.contract_library.data.InferenceServiceStatus
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


}