package views

import javafx.geometry.NodeOrientation
import javafx.geometry.Pos
import javafx.scene.control.TitledPane
import javafx.scene.control.skin.TitledPaneSkin
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

class FluentTitledPaneSkin(control: TitledPane) : TitledPaneSkin(control) {
    private val title = skinnable.lookup(".title") as Pane
    private val arrowButton = skinnable.lookup(".arrow-button") as StackPane
//    private val text = skinnable.lookup(".text") as Node

    init {
        skinnable.alignment = Pos.CENTER_RIGHT
        title.nodeOrientation = NodeOrientation.RIGHT_TO_LEFT
    }
}
