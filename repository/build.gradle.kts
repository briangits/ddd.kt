
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
