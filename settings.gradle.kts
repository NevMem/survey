pluginManagement {
    val kspVersion: String by settings
    val kotlinVersion: String by settings
    plugins {
        id("com.google.devtools.ksp") version kspVersion
    }
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "com.nevmem.survey"

include("backend:converters")
include("backend:core")
include("backend:helpers")
include("backend:common")
include("backend:role")
include("common:data")
include("util")
include("util:exporter")
