package earth.groundctrl.fluent.views

import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.Slider
import javafx.scene.control.skin.SliderSkin
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.util.Duration

/* Slider_themeresources.xaml */
private const val NORMAL_DOT_DIAMETER = 12.0
private const val HOVER_DOT_DIAMETER = 14.0
private const val PRESSED_DOT_DIAMETER = 10.0
private val TRANSITION_DURATION = Duration.millis(150.0)

class FluentSliderSkin(control: Slider) : SliderSkin(control) {
    init {
        val track = skinnable.lookup(".track") as? StackPane
        track?.styleProperty()?.bind(Bindings.createStringBinding(
            {
                val range = skinnable.max - skinnable.min
                val fraction = if (range > 0.0) ((skinnable.value - skinnable.min) / range * 100.0) else 0.0
                "-fx-background-color: linear-gradient(to right, -fx-accent $fraction%, -slider-track-color $fraction%);"
            },
            skinnable.valueProperty(), skinnable.minProperty(), skinnable.maxProperty(),
        ))

        val thumb = skinnable.lookup(".thumb") as? StackPane
        thumb?.let { thumbNode ->
            Platform.runLater {
                fun insetFor(diameter: Double) = (thumbNode.width - diameter) / 2.0

                val dotInset = SimpleDoubleProperty(insetFor(NORMAL_DOT_DIAMETER))
                thumbNode.styleProperty().bind(Bindings.createStringBinding(
                    { "-fx-background-insets: 0, ${dotInset.get()};" },
                    dotInset,
                ))

                fun animateTo(diameter: Double) {
                    Timeline(
                        KeyFrame(TRANSITION_DURATION, KeyValue(dotInset, insetFor(diameter), Interpolator.EASE_BOTH)),
                    ).play()
                }

                thumbNode.addEventFilter(MouseEvent.MOUSE_ENTERED) { animateTo(HOVER_DOT_DIAMETER) }
                thumbNode.addEventFilter(MouseEvent.MOUSE_EXITED) { animateTo(NORMAL_DOT_DIAMETER) }
                thumbNode.addEventFilter(MouseEvent.MOUSE_PRESSED) { animateTo(PRESSED_DOT_DIAMETER) }
                thumbNode.addEventFilter(MouseEvent.MOUSE_RELEASED) {
                    animateTo(if (thumbNode.isHover) HOVER_DOT_DIAMETER else NORMAL_DOT_DIAMETER)
                }

                skinnable.disabledProperty().addListener { _, _, isDisabled ->
                    dotInset.set(insetFor(if (isDisabled) HOVER_DOT_DIAMETER else NORMAL_DOT_DIAMETER))
                }
            }
        }
    }
}
