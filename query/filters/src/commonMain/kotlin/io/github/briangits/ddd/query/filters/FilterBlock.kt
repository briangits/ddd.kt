package io.github.briangits.ddd.query.filters

typealias FilterBlock<T, TProperties> =
    context(Filters<T>) TProperties.() -> Unit
