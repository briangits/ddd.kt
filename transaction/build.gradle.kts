
plugins {
    id("conventions.library.kmp")

    // Publishing
    id("conventions.publishing")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.repository)
        }
    }
}

library {
    name = "transaction"
    description = "Transaction API for persistence.kt"
}
