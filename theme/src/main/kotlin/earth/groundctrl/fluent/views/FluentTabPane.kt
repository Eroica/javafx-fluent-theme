package earth.groundctrl.fluent.views

import javafx.application.Platform
import javafx.collections.ListChangeListener
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.control.skin.TabPaneSkin
import javafx.scene.layout.StackPane

/* TabView.xaml: CloseButton */
private val CLOSE_GLYPH = 0xE711.toChar().toString()

class FluentTabPaneSkin(control: TabPane) : TabPaneSkin(control) {
    init {
        overrideCloseButton()
        skinnable.tabs.addListener(ListChangeListener { Platform.runLater(::overrideCloseButton) })
    }

    fun overrideCloseButton() {
        skinnable.lookupAll(".tab-close-button").forEach { node ->
            val closeButton = node as? StackPane ?: return@forEach
            if (closeButton.children.isEmpty()) {
                closeButton.children.add(Label(CLOSE_GLYPH).apply { styleClass.add("close-icon") })
            }
        }
    }
}
