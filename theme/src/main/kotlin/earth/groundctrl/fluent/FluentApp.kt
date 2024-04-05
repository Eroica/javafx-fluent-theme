package earth.groundctrl.fluent

import earth.groundctrl.fluent.lib.Windows
import javafx.application.Application
import javafx.stage.Stage
import javafx.stage.StageStyle

abstract class FluentApp(
    private val useMica: Boolean = false,
    private val useHeaderBar: Boolean = false
) : Application() {
    abstract fun onCreateStage(primaryStage: Stage)

    override fun init() {
        super.init()
        System.loadLibrary("FluentLib")
        setUserAgentStylesheet("fluent-light.css")
    }

    override fun start(primaryStage: Stage) {
        primaryStage.initStyle(StageStyle.UNIFIED)
        onCreateStage(primaryStage)
        primaryStage.show()
        Windows.subclass(primaryStage.title, useMica, useHeaderBar)
    }
}
