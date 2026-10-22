package io.github.briangits.ddd.query.filters.predicates

data class AllOf<T : Any>(
    val predicates: List<Predicate<T>>
) : LogicalPredicate<T>
