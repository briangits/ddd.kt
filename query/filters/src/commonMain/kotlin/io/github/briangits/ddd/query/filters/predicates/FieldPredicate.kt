package io.github.briangits.ddd.query.filters.predicates

import io.github.briangits.ddd.query.properties.Property

sealed interface FieldPredicate<T : Any, V> : Predicate<T> {
    val property: Property<T, V?>
}
