package earth.groundctrl.fluent.views

import earth.groundctrl.fluent.ui.FluentInterpolator
import javafx.animation.TranslateTransition
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.Skin
import javafx.scene.control.skin.ComboBoxListViewSkin
import javafx.scene.layout.Pane
import javafx.util.Callback
import javafx.util.Duration

class FluentComboBoxSkin<T>(control: ComboBox<T>) : ComboBoxListViewSkin<T>(control) {
    private val slideDown: TranslateTransition by lazy {
        TranslateTransition(Duration.millis(600.0), popupContent).apply {
            interpolator = FluentInterpolator
        }
    }

    override fun show() {
        super.show()
        slideDown.fromY = -skinnable.height
        slideDown.toY = 0.0
        slideDown.play()
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

    override fun createDefaultSkin(): Skin<*>? {
        return FluentComboBoxSkin(this)
    }
}
