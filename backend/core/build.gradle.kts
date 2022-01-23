val ktor_version: String by project
val kotlinVersion: String by project
val logback_version: String by project
val prometeus_version : String by project

plugins {
    application
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "com.nevmem.survey"
version = "0.0.1"
application {
    mainClass.set("com.nevmem.survey.SurveyApplicationKt")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometeus_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.insert-koin:koin-core:3.1.4")
    implementation("io.insert-koin:koin-ktor:3.1.4")
    implementation ("org.jetbrains.exposed:exposed-core:0.33.1")
    implementation ("org.jetbrains.exposed:exposed-dao:0.33.1")
    implementation ("org.jetbrains.exposed:exposed-jdbc:0.33.1")
    implementation ("com.zaxxer:HikariCP:4.0.3")
    implementation ("org.postgresql:postgresql:42.2.23")
    implementation("software.amazon.awssdk:s3:2.17.102")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    implementation(project(":common:data"))
    implementation(project(":backend:auth"))
    implementation(project(":backend:common"))
    implementation(project(":backend:converters"))
    implementation(project(":backend:env"))
    implementation(project(":backend:helpers"))
    implementation(project(":backend:invites"))
    implementation(project(":backend:role"))
    implementation(project(":backend:users"))
}
