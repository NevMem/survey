val versions: Map<String, String> by project

plugins {
    kotlin("jvm")
}

group = "com.nevmem.survey.config.sample"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions["coroutinesCore"]}")
    implementation(project(":backend:config"))
    implementation(project(":util:s3client"))
}
