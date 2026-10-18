package io.github.briangits.persistence.query.properties

import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class ExplodedProperties<R : Any, T : Any> private constructor(
    private val parent: Property<R, T>
) : BaseProperties<T> {

    constructor(
        property: KProperty1<R, T>
    ) : this(parent = Property(listOf(property)) { property.get(it) })

    operator fun <V> KProperty1<T, V>.getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): Property<R, V> =
        Property(parent.path + this) { this.get(parent.get(it)) }

}

typealias explode<R, T> = ExplodedProperties<R, T>

fun <T : explode<*, *>, R> explode(properties: T, block: T.() -> R): R =
    properties.block()
