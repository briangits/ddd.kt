package io.github.briangits.persistence.query.filters.predicates

import io.github.briangits.persistence.query.filters.predicates.operators.equality.EqualityOperator
import io.github.briangits.persistence.query.properties.Property

data class Equality<T : Any, V>(
    override val property: Property<T, V?>,
    val value: V,
    val operator: EqualityOperator
) : FieldPredicate<T, V>
