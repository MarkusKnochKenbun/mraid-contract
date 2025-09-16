package de.kenbun.contract_library

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import de.kenbun.contract_library.PackageContract.MRAID_PACKAGE_NAME
import de.kenbun.contract_library.PackageContract.PERMISSION_ACTIVITY_CLASS_NAME
import de.kenbun.contract_library.PackageContract.RESULT_STRING_EXTRA_KEY

class MraidActivityContract() : ActivityResultContract<Int, String?>() {

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
                Log.d("MraidActivityContract", "RESULT_OK: response: $response")

                return RESULT_OK.toString()
            }

            RESULT_CANCELED -> {
                Log.d("MraidActivityContract", "RESULT_CANCELED")
                return RESULT_CANCELED.toString()
            }

            else -> {
                Log.d("MraidActivityContract", "No matching resultCode!")
                return null
            }
        }

    }
}