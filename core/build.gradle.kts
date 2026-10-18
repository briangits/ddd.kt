plugins {
    id("conventions.library.kmp")

    // Publishing
    id("io.github.briangits.persistence.conventions.publishing")
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
