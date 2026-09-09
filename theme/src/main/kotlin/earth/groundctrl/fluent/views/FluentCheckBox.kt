package earth.groundctrl.fluent.views

import earth.groundctrl.fluent.ui.FluentInterpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.control.CheckBox
import javafx.scene.control.skin.CheckBoxSkin
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.util.Duration

/* CheckBox_themeresources.xaml: CheckBoxCheckedGlyph / CheckBoxIndeterminateGlyph */
private val CHECKED_GLYPH = 0xE73E.toChar().toString()
private val INDETERMINATE_GLYPH = 0xE9AE.toChar().toString()

/* Somewhere in AnimatedAcceptVisualSource, still need to decode it */
private val REVEAL_DURATION = Duration.millis(200.0)

class FluentCheckBoxSkin(control: CheckBox) : CheckBoxSkin(control) {
    init {
        val mark = skinnable.lookup(".mark") as? Pane
        mark?.let {
            val glyph = Text(if (skinnable.isIndeterminate) INDETERMINATE_GLYPH else CHECKED_GLYPH).apply {
                styleClass.add("glyph")
            }
            it.children.add(glyph)

            val revealAnimation = Timeline().apply { setOnFinished { glyph.clip = null } }

            skinnable.selectedProperty().addListener { _, _, isSelected ->
                revealAnimation.stop()
                if (isSelected) {
                    val bounds = glyph.layoutBounds
                    val clip = Rectangle(bounds.minX, bounds.minY, 0.0, bounds.height)
                    glyph.clip = clip
                    revealAnimation.keyFrames.setAll(
                        KeyFrame(Duration.ZERO, KeyValue(clip.widthProperty(), 0.0)),
                        KeyFrame(REVEAL_DURATION, KeyValue(clip.widthProperty(), bounds.width, FluentInterpolator)),
                    )
                    revealAnimation.playFromStart()
                } else {
                    glyph.clip = null
                }
            }

            skinnable.indeterminateProperty().addListener { _, _, isIndeterminate ->
                glyph.text = if (isIndeterminate) INDETERMINATE_GLYPH else CHECKED_GLYPH
            }
        }
    }
}
