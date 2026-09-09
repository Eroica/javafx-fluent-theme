package earth.groundctrl.fluent.views

import javafx.scene.control.Spinner
import javafx.scene.control.skin.SpinnerSkin
import javafx.scene.layout.StackPane
import javafx.scene.text.Text

/* NumberBox.xaml: UpSpinButton/DownSpinButton */
private val UP_GLYPH = 0xE70E.toChar().toString()
private val DOWN_GLYPH = 0xE70D.toChar().toString()

class FluentSpinnerSkin<T>(control: Spinner<T>) : SpinnerSkin<T>(control) {
    init {
        skinnable.isEditable = true
        skinnable.styleClass.add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL)
        (skinnable.lookup(".increment-arrow-button") as? StackPane)?.children?.add(glyph(UP_GLYPH))
        (skinnable.lookup(".decrement-arrow-button") as? StackPane)?.children?.add(glyph(DOWN_GLYPH))
    }

    private fun glyph(character: String) = Text(character).apply { styleClass.add("glyph") }
}
