package io.github.briangits.persistence.query.filters.predicates

data class OneOf<T : Any>(
    val predicates: List<Predicate<T>>
) : LogicalPredicate<T>
