/* Copyright (C) 2023-2026 Eroica
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package earth.groundctrl.fluent

import earth.groundctrl.fluent.lib.Windows
import earth.groundctrl.fluent.ui.setupContextMenuAnimation
import javafx.application.Application
import javafx.application.ColorScheme
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.MapChangeListener
import javafx.scene.layout.Background
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.net.URL

abstract class FluentApp : Application() {
    companion object {
        /**
         * @since v2024.12
         * The properties are static here to modify the `Windows.subclass` call which is done
         * automatically in `start`. Depending on system or app settings, one might want to disable
         * e.g. Mica.
         */
        var useMica: Boolean = true
        var useHeaderBar: Boolean = true

        /**
         * @since v2026.09
         * Fluent theme for this app; this is being watched to react to changes to Windows' theme
         * and accent color.
         */
        private val theme = SimpleObjectProperty<ColorScheme?>()
        private val systemTheme = SimpleObjectProperty<ColorScheme>()

        /**
         * @since v2024.05
         * Sets up the necessary environment to let JavaFX be themed by javafx-fluent-theme:
         * 1. Loads FluentLib.dll,
         * 2. disables sub-pixel antialiasing (in favor of grayscale),
         * 3. if `isFixMica` is true (the default), does an additional check for non-AMD GPUs to
         *    set special JavaFX flags. Otherwise, Windows 11's Mica effect doesn't seem to work
         *    correctly on these systems (when enabled for JavaFX windows).
         *
         * @param isFixMica check for non-AMD GPUs and add necessary flags for supporting Mica.
         */
        @JvmStatic
        fun initialize(isFixMica: Boolean = true) {
            System.loadLibrary("FluentLib")
            System.setProperty("prism.lcdtext", "false")

            if (isFixMica && !Windows.isAmdGpu()) {
                System.setProperty("prism.forceUploadingPainter", "true")
                System.setProperty("javafx.animation.fullspeed", "true")
            }
        }

        /**
         * @since v2026.09
         * Manually set a theme. This should be used for applications that do not want to react to
         * Windows' settings automatically. Set "null" to react to Windows settings again.
         */
        fun setTheme(colorScheme: ColorScheme?) {
            theme.set(colorScheme)
        }
    }

    /** @since v2026.09 Support for dark/light theme and system accent colors */
    private val cssFactory: CssStreamFactory
    private val preferences = Platform.getPreferences()
    private val themeChangeListener: MapChangeListener<String, Any> = MapChangeListener {
        onThemeChanged(FluentColors.Windows())
    }

    init {
        val currentColors = FluentColors.Windows()
        FluentApp.systemTheme.set(currentColors.theme)
        cssFactory = CssStreamFactory(currentColors)
        URL.setURLStreamHandlerFactory(cssFactory)
        setupContextMenuAnimation()
        setAppTheme(currentColors, FluentApp.theme.get() ?: systemTheme.get())
        preferences.addListener(themeChangeListener)
    }

    abstract fun onCreateStage(primaryStage: Stage)

    /**
     * @since v2024.12
     * Callback after `Windows.subclass` has been called, can be used to further set up your app.
     */
    open fun onStageCreated() = Unit

    private lateinit var changeStyleCallback: () -> Unit

    override fun start(primaryStage: Stage) {
        val isDarkTheme = (theme.get() == ColorScheme.DARK
            || (theme.get() == null && systemTheme.get() == ColorScheme.DARK))
        primaryStage.initStyle(StageStyle.UNIFIED)
        onCreateStage(primaryStage)
        wrapContentPane(primaryStage)
        primaryStage.show()
        Windows.subclass(primaryStage.title, useMica, isDarkTheme, useHeaderBar)

        changeStyleCallback = {
            val isDarkTheme = (theme.get() == ColorScheme.DARK
                || (theme.get() == null && systemTheme.get() == ColorScheme.DARK))
            Windows.setDarkThemeFor(primaryStage.title, isDarkTheme)
        }

        FluentApp.theme.addListener {
            val colors = FluentColors.Windows()
            when (val theme = theme.get()) {
                ColorScheme.LIGHT, ColorScheme.DARK -> setAppTheme(colors, theme)
                else -> setAppTheme(colors, colors.theme)
            }
            changeStyleCallback.invoke()
        }
    }

    override fun stop() {
        preferences.removeListener(themeChangeListener)
        super.stop()
    }

    /**
     * @since v2026.09
     * Called when Windows' theme or accent color changes. By default, will re-evaluate the
     * current stylesheet (= change colors dynamically).
     */
    open fun onThemeChanged(newColors: FluentColors) {
        FluentApp.systemTheme.set(newColors.theme)

        if (FluentApp.theme.get() == null) {
            setAppTheme(newColors, newColors.theme)
        } else {
            /* When Windows changes its theme, all Win32 windows will be converted to e.g. dark.
               If the app theme is manually fixed to light, one needs to change back the Dwm style.
               Occasionally, this didn't work, probably due to some kind of race between Windows'
               changes and this callback. Need to investigate ... */
            changeStyleCallback.invoke()
        }
    }

    /**
     * @since v2026.09
     * Makes sure that the Stage's Scene is wrapped by a StackPane. FluentDialog looks for this
     * StackPane to add and remove a "smoke overlay."
     */
    private fun wrapContentPane(primaryStage: Stage) {
        primaryStage.scene.root = StackPane(primaryStage.scene.root).apply {
            background = Background.EMPTY
        }
    }

    private fun setAppTheme(colors: FluentColors, theme: ColorScheme) {
        cssFactory.colors = colors.copy(theme = theme)
        setUserAgentStylesheet(fluentStylesheetUrl())
    }
}
