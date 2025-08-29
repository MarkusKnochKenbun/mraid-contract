package de.kenbun.contract_library.util

import android.os.Build.VERSION.SDK_INT

object AndroidVersionUtil {
    fun isEqualOrHigher(version: Int): Boolean {
        return SDK_INT >= version
    }

    fun isEqual(version: Int): Boolean {
        return SDK_INT == version
    }

    fun isHigher(version: Int): Boolean {
        return SDK_INT > version
    }

    fun isLower(version: Int): Boolean {
        return SDK_INT < version
    }
}