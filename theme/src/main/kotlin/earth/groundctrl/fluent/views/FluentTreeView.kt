package earth.groundctrl.fluent.views

import javafx.scene.control.TreeCell
import javafx.scene.control.skin.TreeCellSkin
import javafx.scene.shape.Rectangle

/* TreeViewItem.xaml: SelectionIndicator */
private const val INDICATOR_WIDTH = 3.0
private const val INDICATOR_HEIGHT = 16.0
private const val INDICATOR_ARC = 4.0

/* TreeViewItemPresenterMargin */
private const val INDICATOR_LEFT_INSET = 4.0

class FluentTreeCellSkin<T>(control: TreeCell<T>) : TreeCellSkin<T>(control) {
    private var indicator: Rectangle? = null

    init {
        indicator = Rectangle(INDICATOR_WIDTH, INDICATOR_HEIGHT).apply {
            styleClass.add("selection-indicator")
            arcWidth = INDICATOR_ARC
            arcHeight = INDICATOR_ARC
        }
        children.add(indicator)
    }

    override fun updateChildren() {
        super.updateChildren()
        indicator?.let { if (!children.contains(it)) children.add(it) }
    }

    override fun layoutChildren(x: Double, y: Double, w: Double, h: Double) {
        super.layoutChildren(x, y, w, h)
        indicator?.let {
            it.x = x + INDICATOR_LEFT_INSET
            it.y = y + (h - INDICATOR_HEIGHT) / 2.0
        }
    }
}
