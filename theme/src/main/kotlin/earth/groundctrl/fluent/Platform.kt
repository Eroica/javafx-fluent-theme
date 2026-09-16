package earth.groundctrl.fluent

import javafx.application.ColorScheme
import javafx.application.Platform
import javafx.scene.paint.Color
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.net.URLStreamHandler
import java.net.URLStreamHandlerFactory
import java.util.Locale
import kotlin.math.roundToInt

/* SolidBackgroundFillColorBase in WinUI's XAML definitions */
private val LIGHT_THEME_BACKDROP = Color.web("#F3F3F3")
private val DARK_THEME_BACKDROP = Color.web("#202020")

private fun mixOverBackdrop(color: Color, alpha: Double, backdrop: Color): Color = Color.color(
    color.red * alpha + backdrop.red * (1 - alpha),
    color.green * alpha + backdrop.green * (1 - alpha),
    color.blue * alpha + backdrop.blue * (1 - alpha),
    1.0,
)

private fun Color.toCss(): String {
    val r = (red * 255).roundToInt()
    val g = (green * 255).roundToInt()
    val b = (blue * 255).roundToInt()

    return if (opacity >= 1.0) {
        String.format("#%02X%02X%02X", r, g, b)
    } else {
        String.format(Locale.ROOT, "rgba(%d,%d,%d,%.3f)", r, g, b, opacity)
    }
}

fun fluentStylesheetUrl(): String = "internal:javafx-fluent-theme-${Math.random()}.css"

data class FluentColors(
    val theme: ColorScheme,
    val foreground: Color,
    val background: Color,
    val accent: Color,
    val accentLight1: Color,
    val accentLight2: Color,
    val accentLight3: Color,
    val accentDark1: Color,
    val accentDark2: Color,
    val accentDark3: Color
) {
    companion object {
        fun Windows(): FluentColors {
            return Platform.getPreferences().run {
                FluentColors(
                    colorScheme,
                    getColor("Windows.UIColor.Foreground").get(),
                    getColor("Windows.UIColor.Background").get(),
                    getColor("Windows.UIColor.Accent").get(),
                    getColor("Windows.UIColor.AccentLight1").get(),
                    getColor("Windows.UIColor.AccentLight2").get(),
                    getColor("Windows.UIColor.AccentLight3").get(),
                    getColor("Windows.UIColor.AccentDark1").get(),
                    getColor("Windows.UIColor.AccentDark2").get(),
                    getColor("Windows.UIColor.AccentDark3").get(),
                )
            }
        }
    }

    val backdrop: Color = if (theme == ColorScheme.DARK) DARK_THEME_BACKDROP else LIGHT_THEME_BACKDROP
    val rootCss = """.root {
    -system-accent-color: ${accent.toCss()};
    -system-accent-color-light1: ${accentLight1.toCss()};
    -system-accent-color-light2: ${accentLight2.toCss()};
    -system-accent-color-light3: ${accentLight3.toCss()};
    -system-accent-color-dark1: ${accentDark1.toCss()};
    -system-accent-color-dark2: ${accentDark2.toCss()};
    -system-accent-color-dark3: ${accentDark3.toCss()};
    -system-accent-color-dark1-90: ${mixOverBackdrop(accentDark1, 0.9, backdrop).toCss()};
    -system-accent-color-dark1-80: ${mixOverBackdrop(accentDark1, 0.8, backdrop).toCss()};
    -system-accent-color-light2-90: ${mixOverBackdrop(accentLight2, 0.9, backdrop).toCss()};
    -system-accent-color-light2-80: ${mixOverBackdrop(accentLight2, 0.8, backdrop).toCss()};
}
"""
    val stylesheet = if (theme == ColorScheme.DARK) "fluent-dark.css" else "fluent-light.css"
}

class CssStreamUrl(
    url: URL,
    private val colors: FluentColors,
) : URLConnection(url) {
    override fun connect() {
    }

    override fun getInputStream(): InputStream {
        val stylesheet = javaClass.getResourceAsStream("/${colors.stylesheet}")
        val builder = StringBuilder()
        builder.append(colors.rootCss)
        stylesheet.use { builder.append(it.readBytes().toString(Charsets.UTF_8)) }

        return ByteArrayInputStream(builder.toString().toByteArray(Charsets.UTF_8))
    }
}

class CssStreamFactory(var colors: FluentColors) : URLStreamHandlerFactory {
    private val streamHandler: URLStreamHandler = object : URLStreamHandler() {
        override fun openConnection(url: URL): URLConnection {
            return CssStreamUrl(url, colors)
        }
    }

    override fun createURLStreamHandler(protocol: String?): URLStreamHandler? {
        return if ("internal" == protocol) {
            streamHandler
        } else null
    }
}
