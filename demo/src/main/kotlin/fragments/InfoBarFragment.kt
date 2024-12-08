package fragments

import controllers.BaseFragment
import earth.groundctrl.fluent.views.InfoBar
import javafx.beans.InvalidationListener
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.ProgressBar
import javafx.scene.control.Spinner
import javafx.util.StringConverter
import java.util.ResourceBundle

class InfoBarFragment : BaseFragment("InfoBarFragment.fxml") {
    private val bundle = ResourceBundle.getBundle("strings")

    private val severityStrings = mapOf(
        bundle.getString("infobar.severity.informational") to InfoBar.Severity.INFORMATIONAL,
        bundle.getString("infobar.severity.success") to InfoBar.Severity.SUCCESS,
        bundle.getString("infobar.severity.warning") to InfoBar.Severity.WARNING,
        bundle.getString("infobar.severity.error") to InfoBar.Severity.ERROR
    )

    @FXML
    private lateinit var infoBar: InfoBar

    @FXML
    private lateinit var isOpen: CheckBox

    @FXML
    private lateinit var isIconVisible: CheckBox

    @FXML
    private lateinit var isClosable: CheckBox

    @FXML
    private lateinit var severity: ComboBox<InfoBar.Severity>

    val converter = object : StringConverter<InfoBar.Severity>() {
        override fun toString(p0: InfoBar.Severity): String? {
            return when (p0) {
                InfoBar.Severity.INFORMATIONAL -> bundle.getString("infobar.severity.informational")
                InfoBar.Severity.SUCCESS -> bundle.getString("infobar.severity.success")
                InfoBar.Severity.WARNING -> bundle.getString("infobar.severity.warning")
                InfoBar.Severity.ERROR -> bundle.getString("infobar.severity.error")
            }
        }

        override fun fromString(p0: String): InfoBar.Severity? {
            return severityStrings[p0]
        }
    }

    override fun onCreateView() {
        super.onCreateView()
        isOpen.selectedProperty().addListener { _, _, newValue -> infoBar.setIsOpen(newValue) }
        isIconVisible.selectedProperty().addListener { _, _, newValue -> infoBar.setIsIconVisible(newValue) }
        isClosable.selectedProperty().addListener { _, _, newValue -> infoBar.setIsCloseable(newValue) }
        severity.valueProperty().addListener { _, _, newValue -> infoBar.severity = newValue }
        infoBar.setOnCloseListener {
            isOpen.isSelected = false
            it.consume()
        }
    }
}
