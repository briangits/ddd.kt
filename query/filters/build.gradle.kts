
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.properties)
        }

        commonTest.dependencies {
            implementation(kt.test)
        }
    }
}

library {
    name = "query-filters"
    description = "Query filters API for ddd.kt"
}
