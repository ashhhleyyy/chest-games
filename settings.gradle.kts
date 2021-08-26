pluginManagement {
    repositories {
        maven {
            name = "FabricMC"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Cotton"
            url = uri("https://server.bbkr.space/artifactory/libs-release")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "Chest Games"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}
