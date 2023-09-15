package views

import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.control.Tooltip
import javafx.util.Duration

class FluentTooltip : Tooltip() {
    private val fadeIn = Timeline(
        KeyFrame(Duration.millis(0.0), KeyValue(opacityProperty(), 0)),
        KeyFrame(Duration.millis(200.0), KeyValue(opacityProperty(), 1.0, Interpolator.EASE_IN))
    )

    override fun show() {
        opacity = 0.0
        super.show()
        fadeIn.play()
    }
}
