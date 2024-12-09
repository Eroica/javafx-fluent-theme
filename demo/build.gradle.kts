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
    version = "23"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":theme"))
    // Replace the line above with this line when using a local package
    // implementation("earth.groundctrl:javafx-fluent-theme:v2024.12a")
}

application {
    mainClass.set("MainKt")
}

runtime {
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    modules = listOf(
        "java.scripting",
        "java.xml",
        "jdk.unsupported"
    )
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
        appVersion = "2024.12"
    }
}
