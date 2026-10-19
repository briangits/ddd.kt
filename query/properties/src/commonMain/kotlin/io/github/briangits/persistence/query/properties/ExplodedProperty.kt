package io.github.briangits.persistence.query.properties

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class ExplodedProperty<R : Any, T : Any> private constructor(
    private val parent: Property<R, T>
) {
    constructor(property: KProperty1<R, T>) : this(
        parent = Property(listOf(property)) { property.get(it) }
    )

    operator fun <V> KProperty1<T, V>.provideDelegate(
        thisRef: ExplodedProperty<R, T>,
        property: KProperty<*>
    ): ReadOnlyProperty<ExplodedProperty<R, T>, Property<R, V>> =
        ReadOnlyProperty  { _, _ ->
            parent.child(this)
        }
}

typealias explode<R, T> = ExplodedProperty<R, T>

inline fun <T : explode<*, *>, R> explode(properties: T, block: T.() -> R): R =
    properties.block()
