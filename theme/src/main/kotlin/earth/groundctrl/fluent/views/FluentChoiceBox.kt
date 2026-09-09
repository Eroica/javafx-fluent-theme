package earth.groundctrl.fluent.views

import javafx.scene.control.ChoiceBox
import javafx.scene.control.skin.ChoiceBoxSkin
import javafx.scene.layout.StackPane
import javafx.scene.text.Text

/* ComboBox_themeresources.xaml: DropDownGlyp */
private val CHEVRON_GLYPH = 0xE70D.toChar().toString()

class FluentChoiceBoxSkin<T>(control: ChoiceBox<T>) : ChoiceBoxSkin<T>(control) {
    init {
        val arrow = skinnable.lookup(".arrow") as? StackPane
        arrow?.children?.add(Text(CHEVRON_GLYPH).apply { styleClass.add("glyph") })
    }
}
