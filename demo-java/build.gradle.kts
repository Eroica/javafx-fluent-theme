plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

javafx {
    version = "22"
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainClass = "earth.groundctrl.fluent.App"
}

dependencies {
    implementation(project(":theme"))
    // Replace the line above with this line when using a local package
    // implementation("earth.groundctrl:javafx-fluent-theme:v2024.04")
}
