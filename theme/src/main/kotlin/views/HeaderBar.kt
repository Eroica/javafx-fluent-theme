package views

import earth.groundctrl.fluent.lib.Windows
import javafx.beans.DefaultProperty
import javafx.beans.InvalidationListener
import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.stage.Screen

@DefaultProperty("children")
class HeaderBar : HBox {
    constructor() : super()

    constructor(vararg children: Node) : super() {
        this.children.addAll(children)
    }

    init {
        styleClass.add("header-bar")
    }
}

class DragPane : Pane() {
    init {
        widthProperty().addListener(InvalidationListener {
            val currentScale = Screen.getPrimary().outputScaleX
            Windows.setDragArea(layoutX.toInt(), width.toInt(), currentScale)
        })
        layoutXProperty().addListener(InvalidationListener {
            val currentScale = Screen.getPrimary().outputScaleX
            Windows.setDragArea(layoutX.toInt(), width.toInt(), currentScale)
        })
    }
}
