package io.github.briangits.ddd.query.filters.predicates

import io.github.briangits.ddd.query.filters.predicates.operators.comparison.ComparisonOperator
import io.github.briangits.ddd.query.properties.Property

data class Comparison<T : Any, V : Comparable<V>>(
    override val property: Property<T, V?>,
    val value: V,
    val operator: ComparisonOperator
) : FieldPredicate<T, V>
