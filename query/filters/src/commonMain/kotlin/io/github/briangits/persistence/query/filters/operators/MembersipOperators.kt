package io.github.briangits.persistence.query.filters.operators

import io.github.briangits.persistence.query.properties.Property

sealed interface MembershipOperator<T : Any, out V> : FieldOperator<T, V> {
    val values: Iterable<V>
}

data class In<T : Any, out V>(
    override val property: Property<T, out V?>,
    override val values: Iterable<V>
) : MembershipOperator<T, V>

data class NotIn<T : Any, out V>(
    override val property: Property<T, out V?>,
    override val values: Iterable<V>
) : MembershipOperator<T, V>
