package earth.groundctrl.fluent;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class App extends FluentApp {
    public static void main(String[] args) {
        FluentApp.initialize(true);
        launch(App.class);
    }

    @Override
    public void onCreateStage(@NotNull Stage primaryStage) {

    }
}
