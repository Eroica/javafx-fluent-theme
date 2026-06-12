plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.jaredsburrows.license") version "0.9.91"
    id("org.beryx.runtime") version "2.0.1"
}

repositories {
    google()
    mavenLocal()
    mavenCentral()
}

javafx {
    version = "26"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":theme"))
    // Replace the line above with this line when using a local package
    // implementation("earth.groundctrl:javafx-fluent-theme:v2025.05")
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
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf("--enable-native-access=javafx.graphics", "--enable-native-access=ALL-UNNAMED")
}

runtime {
    options.set(listOf("--strip-debug", "--compress", "zip-9", "--no-header-files", "--no-man-pages"))
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
            "--copyright", "Copyright (c) 2023-2025 Eroica",
            "--vendor", "GROUNDCTRL",
        )
        appVersion = "2025.05"
        options.addAll("--enable-native-access=javafx.graphics", "--enable-native-access=earth.groundctrl.fluent")
    }
}
