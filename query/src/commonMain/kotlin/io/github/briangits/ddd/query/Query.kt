package io.github.briangits.ddd.query

import io.github.briangits.ddd.query.filters.Filters

class Query<T : Any>(
    val filters: Filters<T>
)
