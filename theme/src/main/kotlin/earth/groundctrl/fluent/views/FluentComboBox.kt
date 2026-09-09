package earth.groundctrl.fluent.views

import earth.groundctrl.fluent.ui.FluentInterpolator
import earth.groundctrl.fluent.ui.REVEAL_FRACTION
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.Skin
import javafx.scene.control.skin.ComboBoxListViewSkin
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.util.Callback
import javafx.util.Duration

/* ComboBox_themeresources.xaml: DropDownGlyph */
private val CHEVRON_GLYPH = 0xE70D.toChar().toString()

class FluentComboBoxSkin<T>(control: ComboBox<T>) : ComboBoxListViewSkin<T>(control) {
    private val revealFraction = SimpleDoubleProperty(REVEAL_FRACTION)

    init {
        (control.lookup(".arrow-button") as? StackPane)?.children?.add(
            Text(CHEVRON_GLYPH).apply { styleClass.add("glyph") }
        )

        (popupContent as? Region)?.let { content ->
            content.clip = Rectangle().apply {
                widthProperty().bind(content.widthProperty())
                heightProperty().bind(content.heightProperty().multiply(revealFraction))
            }
        }
    }

    override fun show() {
        super.show()
        revealFraction.value = REVEAL_FRACTION
        Timeline(
            KeyFrame(Duration.millis(250.0), KeyValue(revealFraction, 1.0, FluentInterpolator))
        ).play()
    }
}

class FluentComboBox<T> : ComboBox<T>() {
    init {
        styleClass.add("fluent-combo-box")
        cellFactory = Callback {
            val indicator = Pane()
            indicator.styleClass.add("indicator")

            object : ListCell<T>() {
                override fun updateItem(item: T, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        text = null
                        graphic = null
                    } else {
                        graphic = indicator
                        text = when {
                            item == null && promptText != null -> promptText
                            converter == null -> item?.toString()
                            else -> converter.toString(item)
                        }
                    }
                }
            }
        }
    }

    override fun createDefaultSkin(): Skin<*> {
        return FluentComboBoxSkin(this)
    }
}
