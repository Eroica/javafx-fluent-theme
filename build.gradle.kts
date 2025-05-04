plugins {
    kotlin("jvm") version "2.1.20"
}

allprojects {
    group = "earth.groundctrl"
    version = "v2025.05"
}

repositories {
    google()
    mavenLocal()
    mavenCentral()
}

kotlin {
   jvmToolchain(23)
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}
