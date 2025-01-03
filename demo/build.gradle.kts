plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.jaredsburrows.license") version "0.9.3"
    id("org.beryx.jlink") version "3.1.1"
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
    // implementation("earth.groundctrl:javafx-fluent-theme:v2024.12b")
}

tasks {
    jpackage {
        finalizedBy("copyDlls")
    }
}

tasks.register<Copy>("copyDlls") {
    from(".")
    include("*.dll")
    into(file(layout.buildDirectory.dir("jpackage/JavaFX Fluent UI Gallery")))
}

application {
    mainModule.set("earth.groundctrl.fluent.demo")
    mainClass.set("earth.groundctrl.fluent.demo.MainKt")
}

jlink {
    options.set(listOf(
        "--strip-debug",
        "--strip-native-commands",
        "--compress", "zip-9",
        "--no-header-files",
        "--no-man-pages"
    ))
    version = "2025.01"
    launcher {
        noConsole = true
    }
    jpackage {
        imageName = "JavaFX Fluent UI Gallery"
        skipInstaller = true
        appVersion = "2025.01"
        imageOptions = listOf(
            "--copyright", "Copyright (c) 2023-2025 Eroica",
            "--vendor", "GROUNDCTRL",
        )
    }
}
