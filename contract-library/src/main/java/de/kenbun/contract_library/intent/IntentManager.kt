package de.kenbun.contract_library.intent

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import de.kenbun.contract_library.MRAIDBroadcastReceiverContract.MRAID_BROADCAST_STOP
import de.kenbun.contract_library.PackageContract.MRAID_PACKAGE_NAME
import de.kenbun.contract_library.PackageContract.PERMISSION_ACTIVITY_CLASS_NAME
import de.kenbun.contract_library.PackageContract.RESULT_STRING_EXTRA_KEY

class IntentManager(private val context: Context) {

    fun startActivityOnResult() {

        val resultLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                manageLauncherResult(result)
            }

        val targetComponent = ComponentName(
            MRAID_PACKAGE_NAME, PERMISSION_ACTIVITY_CLASS_NAME
        )

        val intent = Intent().apply {
            component = targetComponent
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            resultLauncher.launch(intent)
        } else {
            Toast.makeText(
                context, "No app found to handle this request", Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun manageLauncherResult(result: ActivityResult) {

        when (result.resultCode) {

            RESULT_OK -> {
                val response = result.data?.getStringExtra(RESULT_STRING_EXTRA_KEY)
                Log.d("IntentManager", "manageLauncherResult: $response")
                Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
            }

            RESULT_CANCELED -> {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(context, "Unknown result", Toast.LENGTH_SHORT).show()
            }
        }
    }

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