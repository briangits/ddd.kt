package io.github.briangits.ddd.query.properties

import io.github.briangits.ddd.query.properties.property.Property
import io.github.briangits.ddd.query.properties.property.Reference
import io.github.briangits.ddd.query.properties.property.References
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class Properties<T : Any> {
    open operator fun <V> KProperty1<T, V>.provideDelegate(
        thisRef: Properties<T>,
        property: KProperty<*>
    ): ReadOnlyProperty<Properties<T>, Property<T, V>> =
        ReadOnlyProperty { _, _ -> Property(this) }

    open fun <V : Any, VProperties : Properties<V>> reference(
        property: KProperty1<T, V>,
        properties: () -> VProperties
    ): Reference<T, V, VProperties> =
        Reference(Property(property), properties)

    open fun <V : Any, VProperties : Properties<V>> references(
        property: KProperty1<T, V>,
        properties: () -> VProperties
    ): References<T, V, VProperties> = References(Property(property), properties)
}
