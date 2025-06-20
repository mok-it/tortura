package mok.it.tortura

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val excelImportImplemented = false
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun initCBL() {
    // Further testing is needed to determine if this is necessary on Android.
}