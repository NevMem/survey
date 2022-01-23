plugins {
    application
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "com.nevmem.survey.role"
version = "0.0.1"
application {
    mainClass.set("com.nevmem.survey.SurveyApplicationKt")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":backend:common"))
    implementation(project(":common:data"))
}
