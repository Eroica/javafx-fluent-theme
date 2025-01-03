package earth.groundctrl.fluent.demo.views

import javafx.scene.control.PasswordField
import javafx.scene.control.skin.TextFieldSkin

class HashPasswordFieldSkin(control: PasswordField) : TextFieldSkin(control) {
    override fun maskText(txt: String): String {
        return buildString {
            txt.indices.forEach { append("#") }
        }
    }
}
