package io.github.briangits.ddd.query.filters.predicates

import io.github.briangits.ddd.query.filters.predicates.operators.range.RangeOperator
import io.github.briangits.ddd.query.properties.property.Property

data class Range<T : Any, V : Comparable<V>>(
    override val property: Property<T, V?>,
    val start: V,
    val end: V,
    val operator: RangeOperator
) : FieldPredicate<T, V>
