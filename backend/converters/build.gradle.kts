plugins {
    application
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "com.nevmem.survey.converters"
version = "0.0.1"
application {
    mainClass.set("com.nevmem.survey.SurveyApplicationKt")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.insert-koin:koin-core:3.1.4")
    implementation(project(":common:data"))
    implementation(project(":backend:common"))
    implementation(project(":backend:role"))
}
