plugins {
    `version-catalog`

    // Publishing
    id("conventions.publishing")
}


catalog {
    val group = rootProject.group.toString()
    val version = rootProject.version.toString()

    versionCatalog {
        version("persistence", version)

        library("core", group, "core").versionRef("persistence")
        library("uow", group, "uow").versionRef("persistence")

        library("exposed", group, "exposed").versionRef("persistence")
    }
}

library {
    name = "version-catalog"
    description = "A version catalog for persistence.kt"
}
