package io.github.briangits.ddd.query.filters.predicates

import io.github.briangits.ddd.query.filters.predicates.operators.equality.EqualityOperator
import io.github.briangits.ddd.query.properties.property.Property

data class Equality<T : Any, V>(
    override val property: Property<T, V?>,
    val value: V,
    val operator: EqualityOperator
) : FieldPredicate<T, V>
