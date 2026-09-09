package earth.groundctrl.fluent.views

import earth.groundctrl.fluent.ui.FastAnimationDuration
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.control.Tooltip
import javafx.util.Duration

class FluentTooltip : Tooltip() {
    private val fadeIn = Timeline(
        KeyFrame(Duration.millis(FastAnimationDuration), KeyValue(opacityProperty(), 1.0, Interpolator.EASE_IN))
    )
    private val fadeOut = Timeline(
        KeyFrame(Duration.millis(FastAnimationDuration), KeyValue(opacityProperty(), 0.0, Interpolator.EASE_OUT))
    ).apply {
        setOnFinished { super.hide() }
    }

    override fun show() {
        fadeOut.stop()

        if (!isShowing) {
            opacity = 0.0
        }

        super.show()
        fadeIn.playFromStart()
    }

    override fun hide() {
        if (!isShowing) {
            return
        }

        fadeIn.stop()
        fadeOut.playFromStart()
    }
}
