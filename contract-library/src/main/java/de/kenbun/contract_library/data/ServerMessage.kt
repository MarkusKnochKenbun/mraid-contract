package de.kenbun.contract_library.data

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import de.kenbun.contract_library.util.AndroidVersionUtil.isEqualOrHigher
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServerMessage(
    val number: Int, val text: String, val timeStamp: Long
) : Parcelable {

    fun toBundle(): Bundle {
        return Bundle().apply {
            putParcelable(KEY, this@ServerMessage)
        }
    }

    companion object {
        const val KEY = "serverMessage"

        fun fromBundle(bundle: Bundle): ServerMessage? {
            return if (isEqualOrHigher(Build.VERSION_CODES.TIRAMISU)) {
                bundle.getParcelable(KEY, ServerMessage::class.java)
            } else {
                bundle.getParcelable(KEY)
            }
        }

    }
}
