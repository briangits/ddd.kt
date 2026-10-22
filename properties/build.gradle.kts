
plugins {
    id("conventions.library.kmp")

    // Publishing
    id("conventions.publishing")
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(kt.test)
        }
    }
}

library {
    name = "query-properties"
    description = "Query properties for ddd.kt"
}
