package io.github.briangits.ddd.query.filters.predicates

import io.github.briangits.ddd.query.filters.predicates.operators.nullability.NullOperator
import io.github.briangits.ddd.query.properties.property.Property

data class Nullability<T : Any, V>(
    override val property: Property<T, V?>,
    val operator: NullOperator
) : FieldPredicate<T, V>
