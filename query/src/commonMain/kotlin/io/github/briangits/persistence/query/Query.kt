package io.github.briangits.persistence.query

import io.github.briangits.persistence.query.filters.Filters

class Query<T : Any>(
    val filters: Filters<T>
)
