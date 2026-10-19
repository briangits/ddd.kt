package io.github.briangits.persistence.query.filters

typealias FilterBlock<T, TProperties> =
    context(Filters<T>) TProperties.() -> Unit
