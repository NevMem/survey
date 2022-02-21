plugins {
    kotlin("jvm")
}

group = "com.nevmem.survey.role"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":backend:common"))
    implementation(project(":common:data"))
}
