package io.github.briangits.persistence.query.filters.operators

import io.github.briangits.persistence.query.properties.Property

sealed interface FieldOperator<T : Any, out V> : Operator<T> {
    val property: Property<T, out V?>
}

sealed interface ValueOperator<T : Any, out V> : FieldOperator<T, V> {
    val value: V
}
