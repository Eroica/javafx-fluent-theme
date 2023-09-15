plugins {
    `java-library`
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    google()
    mavenLocal()
    mavenCentral()
}

javafx {
    version = "20"
    modules("javafx.controls", "javafx.fxml")
}
