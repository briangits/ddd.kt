package io.github.briangits.persistence.query.filters.operators

sealed interface LogicalOperator<T : Any> : Operator<T>

data class AllOf<T : Any>(
    val operators: List<Operator<T>>
) : LogicalOperator<T>

data class OneOf<T : Any>(
    val operators: List<Operator<T>>
) : LogicalOperator<T>

data class Not<T : Any>(
    val operator: Operator<T>
) : LogicalOperator<T>
