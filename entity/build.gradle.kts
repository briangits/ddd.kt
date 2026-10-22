
plugins {
    id("conventions.library.kmp")
}

group = "${rootProject.group}.entity"

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.properties)
        }
    }
}
