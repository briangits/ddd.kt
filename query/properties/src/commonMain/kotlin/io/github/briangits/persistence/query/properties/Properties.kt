package io.github.briangits.persistence.query.properties

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class Properties<T : Any> {
    operator fun <V> KProperty1<T, V>.provideDelegate(
        thisRef: Properties<T>,
        property: KProperty<*>
    ): ReadOnlyProperty<Properties<T>, Property<T, V>> =
        ReadOnlyProperty { _, _ ->
            Property(this)
        }
}
