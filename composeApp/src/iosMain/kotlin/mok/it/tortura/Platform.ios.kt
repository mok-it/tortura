package mok.it.tortura

import platform.UIKit.UIDevice
import kotlin.system.exitProcess

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun goodNightGoodBye() {
    exitProcess( 0 )
}