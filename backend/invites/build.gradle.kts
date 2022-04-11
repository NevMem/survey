plugins {
    kotlin("jvm")
}

group = "com.nevmem.survey.invites"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.jetbrains.exposed:exposed-dao:0.33.1")
    implementation("io.insert-koin:koin-core:3.1.4")
    implementation(project(":backend:common"))
    implementation(project(":backend:users"))
    implementation(project(":backend:survey"))
    implementation(project(":common:data"))
}
