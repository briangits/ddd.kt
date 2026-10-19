package io.github.briangits.persistence.query.properties

import io.github.briangits.persistence.query.properties.attributes.Attributes
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class Properties<T : Any> : BaseProperties<T> {
    val attributes = Attributes()

    operator fun <V> KProperty1<T, V>.provideDelegate(
        thisRef: Properties<T>,
        property: KProperty<*>
    ): ReadOnlyProperty<Properties<T>, Property<T, V>> =
        ReadOnlyProperty { _, _ ->
            Property(this)
        }
}
