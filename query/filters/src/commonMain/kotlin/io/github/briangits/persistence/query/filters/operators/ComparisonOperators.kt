package io.github.briangits.persistence.query.filters.operators

import io.github.briangits.persistence.query.properties.Property

sealed interface ComparisonOperator<T : Any, V : Comparable<V>> : ValueOperator<T, V>

data class Gt<T : Any, V : Comparable<V>>(
    override val property: Property<T, V?>,
    override val value: V
) : ComparisonOperator<T, V>

data class Gte<T : Any, V : Comparable<V>>(
    override val property: Property<T, V?>,
    override val value: V
) : ComparisonOperator<T, V>

data class Lt<T : Any, V : Comparable<V>>(
    override val property: Property<T, V?>,
    override val value: V
) : ComparisonOperator<T, V>

data class Lte<T : Any, V : Comparable<V>>(
    override val property: Property<T, V?>,
    override val value: V
) : ComparisonOperator<T, V>

