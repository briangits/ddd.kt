package io.github.briangits.persistence.query.dsl

import io.github.briangits.persistence.query.Query
import io.github.briangits.persistence.query.filters.Filters
import io.github.briangits.persistence.query.filters.allOf
import io.github.briangits.persistence.query.properties.Properties

class QueryBuilder<T: Any, TProperties : Properties<T>> {
    internal val filters = Filters<T>()

    fun build(): Query<T> = Query(filters)
}

context(query: QueryBuilder<T, TProperties>)
fun <T : Any, TProperties : Properties<T>> TProperties.where(
    block: context(Filters<T>) TProperties.() -> Unit
) = with(query.filters) { allOf { block() } }
