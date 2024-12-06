package earth.groundctrl.fluent

import earth.groundctrl.fluent.lib.Windows
import javafx.application.Application
import javafx.stage.Stage
import javafx.stage.StageStyle

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
    }

    abstract fun onCreateStage(primaryStage: Stage)

    override fun init() {
        super.init()
        setUserAgentStylesheet("fluent-light.css")
    }

    override fun start(primaryStage: Stage) {
        primaryStage.initStyle(StageStyle.UNIFIED)
        onCreateStage(primaryStage)
        primaryStage.show()
        Windows.subclass(primaryStage.title, useMica, useHeaderBar)
    }
}
