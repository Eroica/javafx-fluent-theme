package controllers

import MainActivity
import fragments.ComboBoxFragment
import fragments.DialogFragment
import fragments.HeaderBarFragment
import fragments.HomeFragment
import fragments.InfoBarFragment
import fragments.ProgressBarFragment
import fragments.SystemBackdropFragment
import javafx.beans.NamedArg
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Hyperlink
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import java.util.*

enum class FragmentType(private val factory: () -> IFragment) {
    Home({ HomeFragment() }),
    Typography({ BaseFragment("TypographyFragment.fxml") }),

    /* Basic input */
    Button({ BaseFragment("ButtonFragment.fxml") }),
    MenuButton({ BaseFragment("MenuButtonFragment.fxml") }),
    Hyperlink({ BaseFragment("HyperlinkFragment.fxml") }),
    ToggleButton({ BaseFragment("ToggleButtonFragment.fxml") }),
    CheckBox({ BaseFragment("CheckBoxFragment.fxml") }),
    ComboBox({ ComboBoxFragment() }),
    RadioButton({ BaseFragment("RadioButtonFragment.fxml") }),
    Slider({ BaseFragment("SliderFragment.fxml") }),

    /* Collections */
    ListView({ BaseFragment("ListViewFragment.fxml") }),
    TreeView({ BaseFragment("TreeViewFragment.fxml") }),
    TableView({ BaseFragment("TableViewFragment.fxml") }),

    /* Date & time */
    DatePicker({ BaseFragment("DatePickerFragment.fxml") }),

    /* Menus & toolbars */
    MenuBar({ BaseFragment("MenuBarFragment.fxml") }),

    /* Dialogs & flyouts */
    Dialog({ DialogFragment() }),

    /* Scrolling */
    ScrollView({ BaseFragment("ScrollViewFragment.fxml") }),

    /* Status & info */
    InfoBar({ InfoBarFragment() }),
    ProgressBar({ ProgressBarFragment() }),
    Tooltip({ BaseFragment("TooltipFragment.fxml") }),

    /* Styles */
    SystemBackdrop({ SystemBackdropFragment() }),

    /* Text */
    Spinner({ BaseFragment("SpinnerFragment.fxml") }),
    PasswordField({ BaseFragment("PasswordFieldFragment.fxml") }),
    Label({ BaseFragment("LabelFragment.fxml") }),
    TextArea({ BaseFragment("TextAreaFragment.fxml") }),

    /* Windowing */
    HeaderBar({ HeaderBarFragment() }),

    Settings({ BaseFragment("SettingsFragment.fxml") });

    operator fun invoke(): IFragment = factory()
}

interface IFragment {
    val view: Pane
    var activity: MainActivity

    fun onCreateView() = Unit
    fun onDestroy() = Unit
}

open class BaseFragment(@NamedArg("resourceId") private val resourceId: String) : VBox(), IFragment {
    override lateinit var activity: MainActivity

    override fun onCreateView() {
        val bundle = ResourceBundle.getBundle("strings")
        FXMLLoader(javaClass.getResource("/fragments/$resourceId"), bundle).apply {
            setRoot(this@BaseFragment)
            setController(this@BaseFragment)
            load()
        }
    }

    override val view: Pane
        get() = this

    @FXML
    private fun onHyperlinkClick(event: ActionEvent) {
        activity.openLink((event.source as Hyperlink).text)
        event.consume()
    }
}
