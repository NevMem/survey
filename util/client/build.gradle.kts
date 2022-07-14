val kspVersion: String by project

plugins {
    kotlin("jvm")
}

group = "com.nevmem.survey.util.client"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
}
