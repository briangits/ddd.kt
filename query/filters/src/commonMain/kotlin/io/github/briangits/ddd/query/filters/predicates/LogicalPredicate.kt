package io.github.briangits.ddd.query.filters.predicates

sealed interface LogicalPredicate<T : Any> : Predicate<T>
