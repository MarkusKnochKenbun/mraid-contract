package de.kenbun.contract_library.intent

import android.content.Context
import android.content.Intent
import android.util.Log
import de.kenbun.contract_library.MRAIDBroadcastReceiverContract.MRAID_BROADCAST_STOP
import de.kenbun.contract_library.PackageContract.MRAID_PACKAGE_NAME


class IntentManager(private val context: Context) {

    fun sendStopIntent() {

        val intent = Intent().apply {
            setPackage(MRAID_PACKAGE_NAME)
            action = MRAID_BROADCAST_STOP
        }

        sendGenericIntent(intent)
    }

    private fun sendGenericIntent(intent: Intent) {
        Log.i("IntentManager", "sendIntentCommand: $intent")
        context.sendBroadcast(intent)
    }

}