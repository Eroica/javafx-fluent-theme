package earth.groundctrl.fluent.views

import javafx.scene.control.MenuButton
import javafx.scene.control.skin.MenuButtonSkin
import javafx.scene.layout.StackPane
import javafx.scene.text.Text

/* DropDownButton.xaml: ChevronIcon */
private val CHEVRON_GLYPH = 0xE96E.toChar().toString()

class FluentMenuButtonSkin(control: MenuButton) : MenuButtonSkin(control) {
    init {
        val arrow = skinnable.lookup(".arrow") as? StackPane
        arrow?.children?.add(Text(CHEVRON_GLYPH).apply { styleClass.add("glyph") })
    }
}
