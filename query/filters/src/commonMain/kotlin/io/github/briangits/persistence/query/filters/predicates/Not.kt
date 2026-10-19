package io.github.briangits.persistence.query.filters.predicates

data class Not<T : Any>(
    val predicate: Predicate<T>
) : LogicalPredicate<T>
