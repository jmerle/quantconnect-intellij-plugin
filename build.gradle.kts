import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.70"
    id("org.jetbrains.intellij") version "0.4.16"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

group = "com.jaspervanmerle.qcij"
version = "1.0.0"

repositories {
    jcenter()
}

dependencies {
    implementation("com.github.kittinunf.fuel:fuel:2.2.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-json-org:2.10.3")
    implementation("org.json:json:20190722")
}

// See https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version = "2019.3"
}

// See https://github.com/jlleitschuh/ktlint-gradle
ktlint {
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
