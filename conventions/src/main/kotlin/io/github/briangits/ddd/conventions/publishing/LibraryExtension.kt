package io.github.briangits.ddd.conventions.publishing

import org.gradle.api.provider.Property

interface LibraryExtension {
    val name: Property<String>
    val description: Property<String>
}
