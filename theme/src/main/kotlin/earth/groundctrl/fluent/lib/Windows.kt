package earth.groundctrl.fluent.lib

import java.io.BufferedReader
import java.io.InputStreamReader

object Windows {
    private external fun isdarkmode(): Boolean

    private external fun setmica(title: String, useMica: Boolean): Int
    private external fun setheaderbar(title: String, useHeaderBar: Boolean): Int
    private external fun setdragarea(x: Int, width: Int, scale: Double)

    external fun subclass(title: String, useMica: Boolean, useHeaderBar: Boolean): Int

    fun setMicaFor(title: String, useMica: Boolean): Int {
        return setmica(title, useMica)
    }

    fun setHeaderBarFor(title: String, useHeaderBar: Boolean): Int {
        return setheaderbar(title, useHeaderBar)
    }

    fun setDragArea(x: Int, width: Int, scale: Double) {
        setdragarea(x, width, scale)
    }

    fun isAmdGpu(): Boolean {
        val process = Runtime.getRuntime().exec(arrayOf("wmic", "PATH", "Win32_videocontroller", "GET", "description"))
        val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
        return "AMD" in output.uppercase()
    }
}
