
plugins {
    id("conventions.library.kmp")
    
    // Publishing
    id("conventions.publishing")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.query)
        }
    }
}

library {
    name = "repository"
    description = "Repository API for ddd.kt"
}
