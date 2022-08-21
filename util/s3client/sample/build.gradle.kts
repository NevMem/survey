plugins {
    kotlin("jvm")
}

group = "com.nevmem.survey.s3client.sample"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":util:s3client"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}