package earth.groundctrl.fluent.views

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.Skin
import javafx.scene.control.skin.ListViewSkin
import javafx.scene.layout.Pane
import javafx.util.Callback

class FluentListViewSkin<T>(control: ListView<T>) : ListViewSkin<T>(control) {
    companion object {
        @JvmStatic
        fun <T> createDefaultCell(): ListCell<T> {
            val indicator = Pane()
            indicator.styleClass.add("indicator")

            return object : ListCell<T>() {
                override fun updateItem(item: T, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        text = null
                        graphic = null
                    } else {
                        graphic = indicator
                        text = item?.toString()
                    }
                }
            }
        }
    }

    init {
        skinnable.styleClass.add("fluent-list-view")
        virtualFlow.cellFactory = Callback { createCell() }
    }

    private fun createCell(): ListCell<T> {
        val cell = if (skinnable.cellFactory != null) skinnable.cellFactory.call(skinnable) else createDefaultCell()
        cell.updateListView(skinnable)

        return cell
    }
}

class FluentListView<T> : ListView<T>() {
    override fun createDefaultSkin(): Skin<*>? {
        return FluentListViewSkin(this)
    }
}
