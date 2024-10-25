/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("buildlogic.scala-application-conventions")
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Specify Java 17
    }
}

dependencies {
    implementation("org.apache.commons:commons-text")
}

application {
    // Define the main class for the application.
    mainClass = "org.example.app.App"
}
