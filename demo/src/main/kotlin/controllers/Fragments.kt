package controllers

import MainActivity
import fragments.HeaderBarFragment
import fragments.HomeFragment
import fragments.SystemBackdropFragment
import javafx.beans.NamedArg
import javafx.fxml.FXMLLoader
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
    ComboBox({ BaseFragment("ComboBoxFragment.fxml") }),
    RadioButton({ BaseFragment("RadioButtonFragment.fxml") }),
    Slider({ BaseFragment("SliderFragment.fxml") }),

    /* Collections */
    ListView({ BaseFragment("ListViewFragment.fxml") }),
    TreeView({ BaseFragment("TreeViewFragment.fxml") }),

    /* Date & time */
    DatePicker({ BaseFragment("DatePickerFragment.fxml") }),

    /* Menus & toolbars */
    MenuBar({ BaseFragment("MenuBarFragment.fxml") }),

    /* Status & info */
    ProgressBar({ BaseFragment("ProgressBarFragment.fxml") }),
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
}
