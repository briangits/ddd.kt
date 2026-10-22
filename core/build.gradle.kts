plugins {
    id("conventions.library.kmp")

    // Publishing
    id("conventions.publishing")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.transaction)
        }
    }
}

library {
    name = "core"
    description = "Core API defintions for ddd.kt"
}
