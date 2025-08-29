package de.kenbun.contract_library.data

import android.os.Parcelable
import androidx.compose.ui.input.key.Key
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServerMessage(
    val number: Int, val text: String, val timeStamp: Long
) : Parcelable {
    companion object {
        const val KEY = "serverMessage"
    }
}
