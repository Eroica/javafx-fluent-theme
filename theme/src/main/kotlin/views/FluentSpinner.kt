package views

import javafx.scene.control.Spinner
import javafx.scene.control.skin.SpinnerSkin

class FluentSpinnerSkin<T>(control: Spinner<T>) : SpinnerSkin<T>(control) {
    init {
        /* Always make the Spinner have split arrows on the right */
        skinnable.styleClass.add("arrows-on-right-horizontal")
    }
}
