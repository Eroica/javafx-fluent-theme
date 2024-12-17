package fragments

import controllers.BaseFragment
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.text.Font

class ComboBoxFragment : BaseFragment("ComboBoxFragment.fxml") {
    companion object {
        private val DEFAULT_FONT = Font(10.0)
    }

    private val fontMap = mutableMapOf<Double, Font>(
        10.0 to DEFAULT_FONT
    )

    @FXML
    private lateinit var fontSize: ComboBox<String>

    @FXML
    private lateinit var fontSizeLabel: Label

    override fun onCreateView() {
        super.onCreateView()
        fontSize.valueProperty().addListener { _, _, newValue ->
            try {
                val cachedFont = fontMap.getOrPut(newValue.toDouble()) { Font(newValue.toDouble()) }
                fontSizeLabel.font = cachedFont
            } catch (_: NumberFormatException) {
                fontSizeLabel.font = DEFAULT_FONT
            }
        }
    }

    override fun onDestroy() {
        fontMap.clear()
        super.onDestroy()
    }
}
