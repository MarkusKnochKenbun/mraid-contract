package de.kenbun.contract_library.util

import android.os.Build.VERSION.SDK_INT
import androidx.annotation.ChecksSdkIntAtLeast


object AndroidVersionUtil {
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isEqualOrHigher(version: Int): Boolean {
        return SDK_INT >= version
    }

    fun isEqual(version: Int): Boolean {
        return SDK_INT == version
    }

    @ChecksSdkIntAtLeast(parameter = 0)
    fun isHigher(version: Int): Boolean {
        return SDK_INT > version
    }

    fun isLower(version: Int): Boolean {
        return SDK_INT < version
    }
}