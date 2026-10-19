package io.github.briangits.persistence.query.filters.predicates

import io.github.briangits.persistence.query.properties.Property

sealed interface FieldPredicate<T : Any, V> : Predicate<T> {
    val property: Property<T, V?>
}
