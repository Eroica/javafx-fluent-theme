import earth.groundctrl.fluent.lib.Windows
import javafx.application.Application
import javafx.stage.Stage
import javafx.stage.StageStyle

abstract class FluentApp(
    private val initialTitle: String,
    private val useMica: Boolean = false,
    private val useHeaderBar: Boolean = false
) : Application() {
    abstract fun onCreateStage(primaryStage: Stage)

    override fun init() {
        super.init()

        /* Mica effect doesn't work reliably (under JavaFX) on non-AMD GPUs */
        if (useMica && !Windows.isAmdGpu()) {
            System.setProperty("prism.forceUploadingPainter", "true")
            System.setProperty("javafx.animation.fullspeed", "true")
        }

        System.loadLibrary("FluentLib")
        setUserAgentStylesheet("fluent-light.css")
    }

    override fun start(primaryStage: Stage) {
        primaryStage.initStyle(StageStyle.UNIFIED)
        primaryStage.title = initialTitle
        onCreateStage(primaryStage)
        primaryStage.show()
        Windows.subclass(primaryStage.title, useMica, useHeaderBar)
    }
}
