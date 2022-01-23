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

include("backend:auth")
include("backend:common")
include("backend:converters")
include("backend:core")
include("backend:env")
include("backend:helpers")
include("backend:invites")
include("backend:role")
include("backend:users")
include("common:data")
include("util")
include("util:exporter")
