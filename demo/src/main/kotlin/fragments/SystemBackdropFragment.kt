package fragments

import controllers.BaseFragment
import javafx.event.ActionEvent
import javafx.fxml.FXML

class SystemBackdropFragment : BaseFragment("SystemBackdropFragment.fxml") {
    @FXML
    private fun onToggleMica(event: ActionEvent) {
        activity.toggleMica()
        event.consume()
    }
}
