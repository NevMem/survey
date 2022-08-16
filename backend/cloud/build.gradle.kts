val versions: Map<String, String> by project
val kotlinVersion: String by project
val ktor_version: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "com.nevmem.survey.cloud"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${versions["kotlinxSerializationJson"]}")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation(project(":backend:env"))
}
