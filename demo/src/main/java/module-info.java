module earth.groundctrl.fluent.demo {
    requires kotlin.stdlib;
    requires javafx.controls;
    requires javafx.fxml;

    requires earth.groundctrl.fluent.theme;

    opens earth.groundctrl.fluent.demo to javafx.fxml;
    opens earth.groundctrl.fluent.demo.controllers to javafx.fxml;
    opens earth.groundctrl.fluent.demo.fragments to javafx.fxml;
    opens earth.groundctrl.fluent.demo.views to javafx.fxml;

    exports earth.groundctrl.fluent.demo;
    exports earth.groundctrl.fluent.demo.views;
}
