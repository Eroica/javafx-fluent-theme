package earth.groundctrl.fluent.demo.fragments

import earth.groundctrl.fluent.demo.controllers.BaseFragment
import javafx.beans.property.ReadOnlyStringProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.stage.Modality
import javafx.stage.Stage

class FullDialog(private val parentWindow: Stage) {
    private val dialog: Stage = FXMLLoader(javaClass.getResource("/earth/groundctrl/fluent/demo/dialogs/FullDialog.fxml")).apply {
        setController(this@FullDialog)
    }.load()

    init {
        dialog.isResizable = false
        dialog.initOwner(parentWindow)
        dialog.initModality(Modality.WINDOW_MODAL)
    }

    fun show() {
        dialog.show()
    }
}

class DialogFragment : BaseFragment("DialogFragment.fxml") {
    private val dialogResult = SimpleStringProperty()

    fun getDialogResult() = dialogResult.get()
    fun dialogResultProperty(): ReadOnlyStringProperty = dialogResult

    private val dialog: Dialog<ButtonType> by lazy {
        FXMLLoader(javaClass.getResource("/earth/groundctrl/fluent/demo/dialogs/Dialog.fxml")).load<Dialog<ButtonType>>().apply {
            isResizable = false
            initOwner(activity.window)
            initModality(Modality.WINDOW_MODAL)
            val buttonBar = dialogPane.lookup(".button-bar") as ButtonBar
            buttonBar.buttonOrder = ButtonBar.BUTTON_ORDER_NONE
        }
    }

    private val fullDialog: FullDialog by lazy { FullDialog(activity.window) }

    @FXML
    private fun onOpenClick(event: ActionEvent) {
        val result = dialog.showAndWait()
        dialogResult.set(result?.get().toString())
        event.consume()
    }

    @FXML
    private fun onOpenFullClick(event: ActionEvent) {
        fullDialog.show()
        event.consume()
    }
}
