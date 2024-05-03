package earth.groundctrl.fluent

import earth.groundctrl.fluent.lib.Windows
import javafx.application.Application
import javafx.stage.Stage
import javafx.stage.StageStyle

abstract class FluentApp(
    private val useMica: Boolean = false,
    private val useHeaderBar: Boolean = false
) : Application() {
    companion object {
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
