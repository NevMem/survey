plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "com.nevmem.survey.survey"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.jetbrains.exposed:exposed-dao:0.33.1")
    implementation("io.insert-koin:koin-core:3.1.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation(project(":backend:common"))
    implementation(project(":backend:helpers"))
    implementation(project(":common:data"))
}
