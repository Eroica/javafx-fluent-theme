plugins {
    kotlin("jvm") version "1.9.10"
}

allprojects {
    group = "earth.groundctrl"
    version = "v2023.09a"
}

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
