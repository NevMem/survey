plugins {
    kotlin("jvm")
}

group = "com.nevmem.survey.s3client"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awssdk:s3:2.17.102")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}
