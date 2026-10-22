package io.github.briangits.ddd.query.properties

import kotlin.reflect.KProperty1

class Property<in R : Any, out T>(
    internal val path: List<KProperty1<*, *>>,
    private val getter: (R) -> T
) {
    constructor(property: KProperty1<R, T>) : this(
        path = listOf(property),
        getter = { property.get(it) }
    )

    fun get(receiver: R): T = getter(receiver)

    override fun equals(other: Any?): Boolean =
        other is Property<*, *> && other.path == path

    override fun hashCode(): Int = path.hashCode()
}

fun <R : Any, P : Any, T> Property(
    parent: Property<R, P>,
    property: KProperty1<P, T>
): Property<R, T> = Property(parent.path + property) { property.get(parent.get(it)) }

fun <R : Any, T : Any, V> Property<R, T>.child(property: KProperty1<T, V>): Property<R, V> =
    Property(this, property)
