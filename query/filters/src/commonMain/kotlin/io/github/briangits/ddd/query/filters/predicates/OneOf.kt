package io.github.briangits.ddd.query.filters.predicates

data class OneOf<T : Any>(
    val predicates: List<Predicate<T>>
) : LogicalPredicate<T>
