plugins {
    application
    kotlin("jvm")
}

group = "com.nevmem.survey.worker"
version = "0.0.1"

application {
    mainClass.set("com.nevmem.survey.worker.MainKt")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.jetbrains.exposed:exposed-core:0.33.1")
    implementation ("org.jetbrains.exposed:exposed-dao:0.33.1")
    implementation ("org.jetbrains.exposed:exposed-jdbc:0.33.1")
    implementation("io.insert-koin:koin-core:3.1.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation ("com.zaxxer:HikariCP:4.0.3")
    implementation ("org.postgresql:postgresql:42.2.23")
    implementation(project(":backend:common"))
    implementation(project(":backend:env"))
    implementation(project(":common:data"))
    implementation(project(":backend:task"))
    implementation(project(":backend:survey"))
    implementation(project(":backend:media"))
}
