package io.github.briangits.ddd.query.filters.predicates

data class Not<T : Any>(
    val predicate: Predicate<T>
) : LogicalPredicate<T>
