package earth.groundctrl.fluent.views

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.text.Text

class InfoBar(title: String = "", message: String = "", severity: Severity = Severity.INFORMATIONAL) : HBox() {
    enum class Severity(val label: String, val icon: String) {
        INFORMATIONAL("informational", "\uF167"),
        SUCCESS("success", "\uEC61"),
        WARNING("warning", "\uE814"),
        ERROR("error", "\uEB90");
    }

    @FXML
    private lateinit var title: Text

    @FXML
    private lateinit var message: Text

    @FXML
    private lateinit var closeButton: Button

    private val isOpen = SimpleBooleanProperty(true)

    fun getIsOpen(): Boolean = isOpen.value
    fun setIsOpen(value: Boolean) {
        isOpen.set(value)
    }

    fun isOpenProperty() = isOpen

    private val isIconVisible = SimpleBooleanProperty(true)

    fun getIsIconVisible(): Boolean = isIconVisible.value
    fun setIsIconVisible(value: Boolean) {
        isIconVisible.set(value)
    }

    fun isIconVisibleProperty() = isIconVisible

    private val isCloseable = SimpleBooleanProperty(true)

    fun getIsCloseable(): Boolean = isCloseable.value
    fun setIsCloseable(value: Boolean) {
        isCloseable.set(value)
    }

    fun isCloseableProperty() = isCloseable

    private val _severity = SimpleObjectProperty<Severity>(Severity.INFORMATIONAL)

    var severity: Severity
        get() = _severity.get()
        set(value) {
            styleClass.remove(_severity.get()?.label)
            _severity.set(value)
            styleClass.add(value.label)
        }

    fun severityProperty() = _severity

    init {
        FXMLLoader(javaClass.getResource("InfoBar.fxml")).apply {
            setRoot(this@InfoBar)
            setController(this@InfoBar)
            load()
        }

        setTitle(title)
        setMessage(message)
        this@InfoBar.severity = severity
    }

    fun getTitle(): String? = title.text

    fun setTitle(title: String) {
        this@InfoBar.title.text = title
    }

    fun getMessage(): String? = message.text

    fun setMessage(message: String) {
        this@InfoBar.message.text = message
    }

    fun setOnCloseListener(action: (ActionEvent) -> Unit) {
        closeButton.onAction = EventHandler { action(it) }
    }
}
