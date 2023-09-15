plugins {
    kotlin("jvm") version "1.9.10"
}

group = "earth.groundctrl.fluent"
version = "2023.09"

repositories {
    google()
    mavenLocal()
    mavenCentral()
}

kotlin {
   jvmToolchain(20)
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}
