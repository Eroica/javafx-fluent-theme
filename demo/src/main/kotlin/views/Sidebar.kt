package views

import controllers.FragmentType
import javafx.beans.NamedArg
import javafx.fxml.FXMLLoader
import javafx.geometry.NodeOrientation
import javafx.scene.control.Separator
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeView
import javafx.scene.control.skin.TreeCellSkin
import javafx.scene.layout.HBox
import javafx.util.Callback

sealed interface ISidebarItem
class SidebarSeparator : ISidebarItem
class SidebarItem(
    @NamedArg("label") val label: String,
    @NamedArg("glyph") val glyph: String? = null,
    @NamedArg("fragment") val fragment: FragmentType? = null
) : ISidebarItem

class SidebarCell : TreeCell<ISidebarItem>() {
    private val item: HBox by lazy {
        FXMLLoader(javaClass.getResource("SidebarItem.fxml")).let {
            it.setController(this)
            it.load()
        }
    }

    private val separator: Separator by lazy {
        FXMLLoader(javaClass.getResource("SidebarSeparator.fxml")).let {
            it.setController(this)
            it.load()
        }
    }

    init {
        /* Make the expansion arrow appear at the right */
        nodeOrientation = NodeOrientation.RIGHT_TO_LEFT

        /* Let whole cell act as click area to expand the item */
        setOnMouseClicked {
            treeItem?.let { it.isExpanded = !it.isExpanded }
        }
    }

    override fun updateItem(item: ISidebarItem?, empty: Boolean) {
        super.updateItem(item, empty)
        if (!empty && item != null) {
            graphic = when (item) {
                is SidebarSeparator -> separator
                is SidebarItem -> this.item
            }
            /* Makes it so that there is no hover effect over the separator item */
            isDisable = item is SidebarSeparator
        } else {
            text = null
            graphic = null
        }
    }
}

/**
 * @since 2023-09-12
 * There is an annoying bug which I think stems from this: https://bugs.openjdk.org/browse/JDK-8119169
 * What I want to have is the cell graphic completely flush to the left side if there is no expansion arrow (= the
 * TreeItem doesn't have any children). However, JavaFX will add padding by looking for the widest arrow-button, or
 * add 18px by default.
 *
 * I didn't take a closer look into this yet, so just override the cell skin with this quickly.
 */
class SidebarCellSkin(control: TreeCell<ISidebarItem>) : TreeCellSkin<ISidebarItem>(control) {
    private val tree = skinnable.treeView

    override fun layoutChildren(x: Double, y: Double, w: Double, h: Double) {
        if (skinnable.treeItem?.children?.isEmpty() == true) {
            layoutLabelInArea((tree.getTreeItemLevel(skinnable.treeItem) - 1) * -16.0, y, w, h)
        } else {
            super.layoutChildren(x, y, w, h)
        }
    }
}

class SidebarCellFactory<T> : Callback<TreeView<T>, SidebarCell> {
    override fun call(param: TreeView<T>?): SidebarCell {
        return SidebarCell()
    }
}
