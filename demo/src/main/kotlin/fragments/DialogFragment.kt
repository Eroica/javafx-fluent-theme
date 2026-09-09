package fragments

import controllers.BaseFragment
import earth.groundctrl.fluent.controllers.FluentDialog
import earth.groundctrl.fluent.controllers.FluentStageDialog
import javafx.beans.property.ReadOnlyStringProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.stage.Modality
import javafx.stage.Stage

class DialogFragment : BaseFragment("DialogFragment.fxml") {
    private val dialogResult = SimpleStringProperty()

    fun getDialogResult() = dialogResult.get()
    fun dialogResultProperty(): ReadOnlyStringProperty = dialogResult

    private val dialog: FluentDialog<ButtonType> by lazy {
        FXMLLoader(javaClass.getResource("/dialogs/Dialog.fxml")).load<FluentDialog<ButtonType>>().apply {
            isResizable = false
            initOwner(activity.window)
            initModality(Modality.WINDOW_MODAL)
            val buttonBar = dialogPane.lookup(".button-bar") as ButtonBar
            buttonBar.buttonOrder = ButtonBar.BUTTON_ORDER_NONE
        }
    }

    private val fullDialog: FluentStageDialog by lazy {
        val fullDialog = FXMLLoader(FluentDemo::class.java.getResource("/dialogs/FullDialog.fxml")).load<FluentStageDialog>()
        fullDialog.isResizable = false
        fullDialog.initOwner(activity.window)
        fullDialog.initModality(Modality.WINDOW_MODAL)
        fullDialog
    }

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
