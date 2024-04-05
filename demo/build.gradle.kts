plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.jaredsburrows.license") version "0.9.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.beryx.runtime") version "1.12.7"
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

dependencies {
    // Replace this with the other line if you do not have Maven package installed locally
    implementation("earth.groundctrl:javafx-fluent-theme:v2023.09a")
    // implementation(project(":theme"))
}

application {
    mainClass.set("MainKt")
}

runtime {
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        noConsole = true
    }
    jpackage {
        imageName = "JavaFX Fluent UI Gallery"
        skipInstaller = true
        imageOptions = listOf(
            "--copyright", "Copyright (c) 2023-2024 Eroica",
            "--vendor", "GROUNDCTRL",
        )
        appVersion = project.version.toString().drop(1)
    }
}
