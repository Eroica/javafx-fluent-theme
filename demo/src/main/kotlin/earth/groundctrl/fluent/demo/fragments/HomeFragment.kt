package earth.groundctrl.fluent.demo.fragments

import earth.groundctrl.fluent.demo.controllers.BaseFragment
import earth.groundctrl.fluent.demo.controllers.FragmentType
import earth.groundctrl.fluent.demo.IViewEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.input.MouseEvent

class HomeFragment : BaseFragment("HomeFragment.fxml") {
    @FXML
    private fun onHomeCardClick(event: MouseEvent) {
        activity.openLink((event.source as Node).userData.toString())
        event.consume()
    }

    @FXML
    private fun onSampleClick(event: MouseEvent) {
        // TODO The Activity will replace the fragment, but won't select it in the sidebar
        activity.on(IViewEvent.Select((event.source as Node).userData as FragmentType))
        event.consume()
    }
}
