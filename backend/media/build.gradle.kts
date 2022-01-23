plugins {
    kotlin("jvm")
}

group = "com.nevmem.survey.media"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.jetbrains.exposed:exposed-dao:0.33.1")
    implementation("software.amazon.awssdk:s3:2.17.102")
    implementation(project(":backend:common"))
    implementation(project(":backend:env"))
    implementation(project(":common:data"))
}
