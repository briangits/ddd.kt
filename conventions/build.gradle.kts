import org.gradle.accessors.dm.LibrariesForKt

plugins {
     alias(kt.plugins.jvm)
    `java-gradle-plugin`
}

group = "io.github.briangits.persistence.conventions"
version = "0.0.1"

gradlePlugin {
    plugins {
        create("publishing") {
            id = "$group.publishing"
            implementationClass = "$group.publishing.PublishPlugin"
        }

        create("publishing-alias") {
            id = "conventions.publishing"
            implementationClass = "$group.publishing.PublishPlugin"
        }

        create("kmp-library") {
            id = "conventions.library.kmp"
            implementationClass = "$group.library.kmp.KMPLibraryPlugin"
        }
    }
}

fun DependencyHandlerScope.plugin(plugin: Provider<PluginDependency>) {
    val artifact =
        plugin.get().let {
            "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version.requiredVersion}"
        }

    implementation(artifact)
}

fun DependencyHandlerScope.plugin(plugin: LibrariesForKt.MultiplatformPluginAccessors) =
    plugin(plugin.asProvider())

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())

    plugin(kt.plugins.multiplatform)
    plugin(kt.plugins.multiplatform.android)

    plugin(libutils.plugins.mavenPublish)
}
