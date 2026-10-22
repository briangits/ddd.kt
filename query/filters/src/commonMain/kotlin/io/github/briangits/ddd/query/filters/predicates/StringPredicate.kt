package io.github.briangits.ddd.query.filters.predicates

import io.github.briangits.ddd.query.filters.predicates.operators.string.StringOperator
import io.github.briangits.ddd.query.properties.Property

data class StringPredicate<T : Any>(
    override val property: Property<T, String?>,
    val value: String,
    val operator: StringOperator
) : FieldPredicate<T, String>
