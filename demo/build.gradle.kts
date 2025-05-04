plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.jaredsburrows.license") version "0.9.3"
    id("com.dua3.gradle.runtime") version "1.13.1-patch-1"
}

repositories {
    google()
    mavenLocal()
    mavenCentral()
}

javafx {
    version = "24"
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
    }
}
