plugins {
    kotlin("jvm") version "2.1.0"
}

allprojects {
    group = "earth.groundctrl"
    version = "v2025.01"
}

repositories {
    google()
    mavenLocal()
    mavenCentral()
}

kotlin {
   jvmToolchain(22)
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}
