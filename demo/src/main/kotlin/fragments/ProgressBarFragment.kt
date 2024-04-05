package fragments

import controllers.BaseFragment
import javafx.beans.InvalidationListener
import javafx.fxml.FXML
import javafx.scene.control.ProgressBar
import javafx.scene.control.Spinner

class ProgressBarFragment : BaseFragment("ProgressBarFragment.fxml") {
    @FXML
    private lateinit var determinateProgress: ProgressBar

    @FXML
    private lateinit var progressSpinner: Spinner<Int>

    override fun onCreateView() {
        super.onCreateView()
        progressSpinner.valueProperty().addListener(InvalidationListener {
            determinateProgress.progress = progressSpinner.value / 100.0
        })
    }
}
