package io.github.briangits.persistence.exposed.query

import io.github.briangits.persistence.exposed.query.filters.compile
import io.github.briangits.persistence.exposed.relations.Relations
import io.github.briangits.persistence.query.dsl.QueryBlock
import io.github.briangits.persistence.query.dsl.QueryBuilder
import io.github.briangits.persistence.query.properties.Properties
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
