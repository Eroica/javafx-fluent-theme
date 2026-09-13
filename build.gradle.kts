plugins {
    kotlin("jvm") version "2.4.0"
}

allprojects {
    group = "earth.groundctrl"
    version = "v2026.09"
}

repositories {
    google()
    mavenLocal()
    mavenCentral()
}

kotlin {
   jvmToolchain(24)
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}
