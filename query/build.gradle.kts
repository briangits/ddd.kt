
plugins {
    id("conventions.library.kmp")
    
    // Publishing
    id("conventions.publishing")
}

subprojects {
    plugins.apply("conventions.library.kmp")
    plugins.apply("conventions.publishing")

    group = "${rootProject.group}.query"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.query.properties)
            api(projects.query.filters)
            api(projects.query.pagination)
        }
    }
}
