package io.github.briangits.ddd.query.properties

import io.github.briangits.ddd.query.properties.property.Property
import io.github.briangits.ddd.query.properties.property.Reference
import io.github.briangits.ddd.query.properties.property.References
import io.github.briangits.ddd.query.properties.property.child
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class ExplodedProperty<R : Any, T : Any> private constructor(
    private val parent: Property<R, T>
) {
    constructor(property: KProperty1<R, T>) : this(
        parent = Property(listOf(property)) { property.get(it) }
    )

    protected fun <V> property(property: KProperty1<T, V>): Property<R, V> =
        parent.child(property)

    operator fun <V> KProperty1<T, V>.provideDelegate(
        thisRef: ExplodedProperty<R, T>,
        property: KProperty<*>
    ): ReadOnlyProperty<ExplodedProperty<R, T>, Property<R, V>> =
        ReadOnlyProperty  { _, _ -> property(this) }

    open fun <V : Any, VProperties : Properties<V>> reference(
        property: KProperty1<T, V>,
        properties: () -> VProperties
    ): Reference<R, V, VProperties> =
        Reference(property(property), properties)

    open fun <V : Any, VProperties : Properties<V>> references(
        property: KProperty1<T, V>,
        properties: () -> VProperties
    ): References<R, V, VProperties> =
        References(property(property), properties)
}

typealias explode<R, T> = ExplodedProperty<R, T>

inline fun <T : explode<*, *>, R> explode(properties: T, block: T.() -> R): R =
    properties.block()
