package io.github.briangits.persistence.query.filters.operators

import io.github.briangits.persistence.query.properties.Property

sealed interface RangeOperator<T : Any, V : Comparable<V>> : FieldOperator<T, V> {
    val start: V
    val end: V
}

data class Between<T : Any, V : Comparable<V>>(
    override val property: Property<T, V?>,
    override val start: V,
    override val end: V
) : RangeOperator<T, V>
