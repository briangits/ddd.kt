package io.github.briangits.persistence.query.dsl

typealias QueryBlock<T, TProperties> =
    context(QueryBuilder<T, TProperties>) TProperties.() -> Unit
