pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()


    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {  url = uri("https://jitpack.io") }
    }
}

rootProject.name = "GoGit"
include(":androidApp")
include(":networkmodule")
include(":shared")
