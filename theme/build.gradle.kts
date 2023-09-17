plugins {
    `java-library`
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("maven-publish")
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "javafx-fluent-theme"
            from(components["java"])

            pom {
                name.set("javafx-fluent-theme")
                description.set("A custom theme for JavaFX following Windows 11's designs")
                url.set("https://github.com/Eroica/javafx-fluent-theme")
                licenses {
                    license {
                        name.set("zlib License")
                        url.set("https://raw.githubusercontent.com/Eroica/javafx-fluent-theme/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("Eroica")
                        name.set("Eroica")
                    }
                }
            }
        }
    }
}
