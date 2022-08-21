val versions: Map<String, String> by project
val logback_version: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "com.nevmem.survey.config"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":util:s3client"))
    implementation(project(":backend:cloud"))
    implementation(project(":common:data"))
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${versions["kotlinxSerializationJson"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions["coroutinesCore"]}")
}
