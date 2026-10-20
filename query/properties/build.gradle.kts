
kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(kt.test)
        }
    }
}

library {
    name = "query-properties"
    description = "Query properties for persistence.kt"
}
