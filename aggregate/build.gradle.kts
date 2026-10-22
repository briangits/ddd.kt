
plugins {
    id("conventions.library.kmp")
}

group = "${rootProject.group}.aggregate"

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.entity)
        }
    }
}
