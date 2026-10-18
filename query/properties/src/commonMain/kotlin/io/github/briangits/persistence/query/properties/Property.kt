package io.github.briangits.persistence.query.properties

import kotlin.reflect.KProperty1

class Property<in R : Any, out T>(
    internal val path: List<KProperty1<*, *>>,
    private val getter: (R) -> T
) {
    fun get(receiver: R): T = getter(receiver)

    override fun equals(other: Any?): Boolean =
        other is Property<*, *> && other.path == path

    override fun hashCode(): Int =
        path.hashCode()
}
