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
    description = "Core persistence abstraction API for persistence.kt"
}
