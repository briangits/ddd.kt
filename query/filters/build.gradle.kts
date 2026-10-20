
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.query.properties)
        }

        commonTest.dependencies {
            implementation(kt.test)
        }
    }
}

library {
    name = "query-filters"
    description = "Query filters for persistence.kt"
}
