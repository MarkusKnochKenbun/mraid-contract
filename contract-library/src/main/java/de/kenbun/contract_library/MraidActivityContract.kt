package de.kenbun.contract_library

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import de.kenbun.contract_library.PackageContract.MRAID_PACKAGE_NAME
import de.kenbun.contract_library.PackageContract.PERMISSION_ACTIVITY_CLASS_NAME
import de.kenbun.contract_library.PackageContract.RESULT_STRING_EXTRA_KEY

class MraidActivityContract(private val context: Context) : ActivityResultContract<Int, String?>() {

    override fun createIntent(context: Context, input: Int): Intent {

        val targetComponent = ComponentName(
            MRAID_PACKAGE_NAME, PERMISSION_ACTIVITY_CLASS_NAME
        )

        val intent = Intent().apply {
            component = targetComponent
        }

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {

        when (resultCode) {

            RESULT_OK -> {
                val response = intent?.getStringExtra(RESULT_STRING_EXTRA_KEY)
                Log.d("IntentManager", "manageLauncherResult: $response")

                return response
            }

            RESULT_CANCELED -> {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                return null
            }

            else -> {
                Toast.makeText(context, "Unknown result", Toast.LENGTH_SHORT).show()
                return null
            }
        }

    }
}