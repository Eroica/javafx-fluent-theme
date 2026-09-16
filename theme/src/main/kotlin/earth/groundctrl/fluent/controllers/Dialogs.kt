package earth.groundctrl.fluent.controllers

import earth.groundctrl.fluent.fluentStylesheetUrl
import earth.groundctrl.fluent.ui.FastAnimationDuration
import earth.groundctrl.fluent.ui.setupContextMenuAnimation
import javafx.animation.FadeTransition
import javafx.application.Application.setUserAgentStylesheet
import javafx.beans.InvalidationListener
import javafx.scene.control.Dialog
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.stage.Window
import javafx.util.Duration

open class FluentDialog<R> : Dialog<R>() {
    private var smokeOverlay: SmokeOverlay? = null

    init {
        initStyle(StageStyle.TRANSPARENT)
        setUserAgentStylesheet(fluentStylesheetUrl())
        setupContextMenuAnimation()
        dialogPane.scene.fill = Color.TRANSPARENT

        setOnShowing {
            SmokeOverlay(owner)
                .also { smokeOverlay = it }
                .show()
        }
        setOnShown {
            dialogPane.buttonTypes.find { it.buttonData.isDefaultButton }
                ?.let { dialogPane.lookupButton(it) }
                ?.styleClass?.add("accent")
        }
        setOnHidden {
            smokeOverlay?.remove()
            smokeOverlay = null
        }
    }
}

open class FluentStageDialog : Stage() {
    private var smokeOverlay: SmokeOverlay? = null

    private val setupSceneListener: InvalidationListener = InvalidationListener {
        scene.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            if (event.code == KeyCode.ESCAPE) {
                close()
                event.consume()
            }
        }

        sceneProperty().removeListener(setupSceneListener)
    }

    init {
        initStyle(StageStyle.TRANSPARENT)
        setUserAgentStylesheet(fluentStylesheetUrl())
        setupContextMenuAnimation()

        sceneProperty().addListener(setupSceneListener)
        setOnShowing {
            SmokeOverlay(owner)
                .also { smokeOverlay = it }
                .show()
        }
        setOnHidden {
            smokeOverlay?.remove()
            smokeOverlay = null
        }
    }
}

private class SmokeOverlay(owner: Window): Region() {
    private val host: StackPane = owner.scene.root as StackPane
    private val fadeIn = FadeTransition(Duration.millis(FastAnimationDuration), this).apply {
        fromValue = 0.0
        toValue = 1.0
    }

    init {
        styleClass.add("dialog-smoke-layer")
    }

    fun show() {
        host.children?.add(this)
        fadeIn.play()
    }

    fun remove() {
        host.children?.remove(this)
    }
}
