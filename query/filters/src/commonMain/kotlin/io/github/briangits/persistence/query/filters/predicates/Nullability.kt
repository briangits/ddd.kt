package io.github.briangits.persistence.query.filters.predicates

import io.github.briangits.persistence.query.filters.predicates.operators.nullability.NullOperator
import io.github.briangits.persistence.query.properties.Property

data class Nullability<T : Any, V>(
    override val property: Property<T, V?>,
    val operator: NullOperator
) : FieldPredicate<T, V>
