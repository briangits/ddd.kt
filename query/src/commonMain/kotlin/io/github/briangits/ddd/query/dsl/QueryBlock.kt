package io.github.briangits.ddd.query.dsl

typealias QueryBlock<T, TProperties> =
    context(QueryBuilder<T, TProperties>) TProperties.() -> Unit
