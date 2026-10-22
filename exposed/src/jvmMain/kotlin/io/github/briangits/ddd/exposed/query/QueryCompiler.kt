package io.github.briangits.ddd.exposed.query

import io.github.briangits.ddd.exposed.query.filters.compile
import io.github.briangits.ddd.exposed.relations.Relations
import io.github.briangits.ddd.query.dsl.QueryBlock
import io.github.briangits.ddd.query.dsl.QueryBuilder
import io.github.briangits.ddd.query.properties.Properties
import org.jetbrains.exposed.v1.core.Op

internal data class ExposedQuery(
    val where: Op<Boolean>
)

context(relations: Relations<T>, properties: () -> TProperties)
internal fun <
    T : Any,
    TProperties : Properties<T>
> QueryBlock<T, TProperties>.build(): ExposedQuery {
    val builder = QueryBuilder<T, TProperties>()

    context(builder) { this(properties()) }

    val query = builder.build()

    return ExposedQuery(
        where = query.filters.compile()
    )
}
