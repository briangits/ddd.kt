package io.github.briangits.persistence.query.filters.predicates

import io.github.briangits.persistence.query.filters.predicates.operators.string.StringOperator
import io.github.briangits.persistence.query.properties.Property

data class StringPredicate<T : Any>(
    override val property: Property<T, String?>,
    val value: String,
    val operator: StringOperator
) : FieldPredicate<T, String>
