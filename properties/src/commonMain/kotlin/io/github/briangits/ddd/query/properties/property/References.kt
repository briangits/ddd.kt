package io.github.briangits.ddd.query.properties.property

import io.github.briangits.ddd.query.properties.Properties
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class References<in R : Any, T : Any, TProperties : Properties<T>>(
    property: Property<R, T>,
    val properties: () -> TProperties
) : Property<R, T>(property.path, property::get) {
    operator fun provideDelegate(
        thisRef: Any,
        property: KProperty<*>
    ): ReadOnlyProperty<Any, References<R, T, TProperties>> =
        ReadOnlyProperty { _, _ -> this }
}
