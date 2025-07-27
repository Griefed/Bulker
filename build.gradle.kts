plugins {
    application
    kotlin("jvm") version "2.2.0"
    id("io.github.file5.guidesigner") version "1.0.2"
    id("com.gradleup.shadow") version "9.0.0-rc2"
}

group = "de.griefed"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.intellij:forms_rt:7.0.3")
    implementation("com.jetbrains.intellij.java:java-gui-forms-rt:+")
    implementation("com.jgoodies:forms:1.1-preview")
    implementation("commons-io:commons-io:2.20.0")

    implementation("com.formdev:flatlaf:3.6")
    implementation("com.formdev:flatlaf-intellij-themes:3.6")
    implementation("com.formdev:flatlaf-fonts-jetbrains-mono:2.304")
}

application {
    mainClass.set("de.griefed.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}