package earth.groundctrl.fluent.demo.fragments

import earth.groundctrl.fluent.demo.controllers.BaseFragment
import javafx.event.ActionEvent
import javafx.fxml.FXML

class HeaderBarFragment : BaseFragment("HeaderBarFragment.fxml") {
    @FXML
    private fun onToggleHeaderBar(event: ActionEvent) {
        activity.toggleHeaderBar()
        event.consume()
    }
}
