pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()

        includeBuild("conventions")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        google()
    }

    versionCatalogs {
        create("kt").from(files("version-catalogs/kotlin.versions.toml"))
        create("kotlinx").from(files("version-catalogs/kotlinx.versions.toml"))
        create("exposed").from(files("version-catalogs/exposed.versions.toml"))
        create("libutils").from(files("version-catalogs/libutils.versions.toml"))
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ddd-kt"

include(":entity")

include(":query")
include(":query:properties")
include(":query:filters")
include(":query:pagination")

include(":repository")

include(":transaction")

include(":core")

include(":unit-of-work")

include(":exposed")

include(":version-catalog")
