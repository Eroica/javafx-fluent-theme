package earth.groundctrl.fluent.ui

import java.util.WeakHashMap
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.property.SimpleDoubleProperty
import javafx.collections.ListChangeListener
import javafx.scene.control.ContextMenu
import javafx.scene.layout.Region
import javafx.scene.shape.Rectangle
import javafx.stage.Window
import javafx.util.Duration

const val REVEAL_FRACTION = 0.5

private val revealFractions = WeakHashMap<ContextMenu, SimpleDoubleProperty>()

fun setupContextMenuAnimation() {
    Window.getWindows().addListener(ListChangeListener { change ->
        while (change.next()) {
            if (!change.wasAdded()) {
                continue
            }
            for (window in change.addedSubList) {
                if (window !is ContextMenu) {
                    continue
                }
                val root = window.scene?.root as? Region ?: continue

                val revealFraction = revealFractions.getOrPut(window) {
                    val fraction = SimpleDoubleProperty(REVEAL_FRACTION)
                    root.clip = Rectangle().apply {
                        widthProperty().bind(root.widthProperty())
                        heightProperty().bind(root.heightProperty().multiply(fraction))
                    }
                    fraction
                }
                revealFraction.value = REVEAL_FRACTION
                Timeline(
                    KeyFrame(Duration.millis(250.0), KeyValue(revealFraction, 1.0, FluentInterpolator))
                ).play()
            }
        }
    })
}
