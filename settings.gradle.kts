pluginManagement {
    val kspVersion: String by settings
    val kotlinVersion: String by settings
    plugins {
        id("com.google.devtools.ksp") version kspVersion
        id("org.jetbrains.kotlin.jvm") version "1.5.30"
    }
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "com.nevmem.survey"

include("android:app")
include("android:network")
include("android:preferences")
include("android:report")
include("android:settings")
include("android")
include("backend:auth")
include("backend:common")
include("backend:converters")
include("backend:core")
include("backend:env")
include("backend:fs")
include("backend:helpers")
include("backend:invites")
include("backend:media")
include("backend:push:client")
include("backend:push:pushapi")
include("backend:push")
include("backend:role")
include("backend:survey")
include("backend:task")
include("backend:users")
include("backend:worker:api")
include("backend:worker")
include("common:data")
include("util:exporter")
include("util")
