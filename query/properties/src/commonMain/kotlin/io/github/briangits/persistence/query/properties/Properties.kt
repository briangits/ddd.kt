package io.github.briangits.persistence.query.properties

import io.github.briangits.persistence.query.properties.attributes.Attributes
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class Properties<T : Any> : BaseProperties<T> {
    val attributes = Attributes()

    operator fun <V> KProperty1<T, V>.getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): Property<T, V> =
        Property(listOf(this)) { this.get(it) }
}
