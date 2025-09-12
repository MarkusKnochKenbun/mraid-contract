package de.kenbun.contract_library

import android.net.Uri
import androidx.core.net.toUri
import de.kenbun.contract_library.PackageContract.MRAID_PACKAGE_NAME

object PackageContract {
    const val MRAID_PACKAGE_NAME = "de.kenbun.mraid"
    const val PERMISSION_ACTIVITY_CLASS_NAME = "$MRAID_PACKAGE_NAME.PermissionActivity"
    const val RESULT_STRING_EXTRA_KEY = "mraidResultStringExtraKey"

    const val DATA_ACCESS_PERMISSION = "$MRAID_PACKAGE_NAME.DATA_ACCESS_PERMISSION"

    const val START_ACTIVITY_REQUEST_CODE = 1
}

object DataContentProviderContract {
    const val DATA_PROVIDER_AUTHORITY =
        "$MRAID_PACKAGE_NAME.feature.contentprovider.datacontentprovider"

    const val CURRENT_TRANSCRIPTION_PATH = "currentTranscription"
    const val CURRENT_TRANSCRIPTION_CODE = 1

    const val REQUEST_SERVICE_STATUS_PATH = "requestServiceStatus"
    const val REQUEST_SERVICE_STATUS_CODE = 2

    val CONTENT_URI_CURRENT_TRANSCRIPTION: Uri =
        "content://$DATA_PROVIDER_AUTHORITY/$CURRENT_TRANSCRIPTION_PATH".toUri()

    val CONTENT_URI_SERVICE_STATUS: Uri =
        "content://$DATA_PROVIDER_AUTHORITY/$REQUEST_SERVICE_STATUS_PATH".toUri()
}

object MRAIDBroadcastReceiverContract {
    const val MRAID_BROADCAST_STOP = "$MRAID_PACKAGE_NAME.action.STOP"
}
