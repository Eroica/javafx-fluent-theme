import controllers.FragmentType
import controllers.IFragment
import earth.groundctrl.fluent.lib.Windows
import javafx.application.HostServices
import javafx.beans.InvalidationListener
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.TreeView
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import views.ISidebarItem
import views.SidebarItem

sealed interface IViewEvent {
    class Select(val fragment: FragmentType) : IViewEvent
}

class MainActivity(
    val windowTitle: String,
    private val hostServices: HostServices
) : VBox() {
    @FXML
    private lateinit var sidebar: TreeView<ISidebarItem>

    @FXML
    private lateinit var fragmentContainer: StackPane

    private val isMicaEffect = SimpleBooleanProperty(true)
    fun getIsMicaEffect() = isMicaEffect.get()
    fun isMicaEffectProperty(): ReadOnlyBooleanProperty = isMicaEffect

    private val isHeaderBar = SimpleBooleanProperty(true)
    fun getIsHeaderBar() = isHeaderBar.get()
    fun isHeaderBarProperty(): ReadOnlyBooleanProperty = isHeaderBar

    private var fragment: IFragment? = null

    init {
        FXMLLoader(javaClass.getResource("MainActivity.fxml")).apply {
            setRoot(this@MainActivity)
            setController(this@MainActivity)
            load()
        }

        sidebar.selectionModel.selectedItemProperty()
            .addListener { _, _, treeItem ->
                (treeItem.value as? SidebarItem)?.fragment?.let { on(IViewEvent.Select(it)) }
            }

        isMicaEffectProperty().addListener(InvalidationListener {
            if (getIsMicaEffect()) {
                styleClass.add("use-mica")
            } else {
                styleClass.remove("use-mica")
            }
        })

        on(IViewEvent.Select(FragmentType.Home))
    }

    fun on(event: IViewEvent) {
        when (event) {
            is IViewEvent.Select -> {
                replaceFragment(event.fragment)
            }
        }
    }

    fun toggleMica() {
        Windows.setMicaFor(windowTitle, !getIsMicaEffect().also { isMicaEffect.set(!it) })
    }

    fun toggleHeaderBar() {
        Windows.setHeaderBarFor(windowTitle, !getIsHeaderBar().also { isHeaderBar.set(!it) })
    }

    fun openLink(uri: String) {
        hostServices.showDocument(uri)
    }

    @FXML
    private fun onSettingsClick(event: ActionEvent) {
        replaceFragment(FragmentType.Settings)
        event.consume()
    }

    private fun replaceFragment(fragment: FragmentType) {
        this.fragment?.onDestroy()
        fragmentContainer.children.clear()
        val newFragment = fragment()
        newFragment.activity = this
        newFragment.onCreateView()
        fragmentContainer.children.add(newFragment.view)
    }
}
