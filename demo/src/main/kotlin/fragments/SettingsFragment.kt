package fragments

import controllers.BaseFragment
import earth.groundctrl.fluent.FluentApp
import javafx.application.ColorScheme
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox

class SettingsFragment : BaseFragment("SettingsFragment.fxml") {
    @FXML
    private lateinit var themeChoice: ChoiceBox<String>

    override fun onCreateView() {
        super.onCreateView()
        themeChoice.valueProperty().addListener { _, _, newTheme ->
            when (newTheme) {
                "Light" -> FluentApp.setTheme(ColorScheme.LIGHT)
                "Dark" -> FluentApp.setTheme(ColorScheme.DARK)
                "System" -> FluentApp.setTheme(null)
            }
        }
    }
}
