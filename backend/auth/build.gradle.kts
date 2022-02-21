val ktor_version: String by project

plugins {
    kotlin("jvm")
}

group = "com.nevmem.survey.auth"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation(project(":backend:common"))
    implementation(project(":backend:env"))
}
