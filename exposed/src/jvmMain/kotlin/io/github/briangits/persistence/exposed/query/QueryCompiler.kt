package io.github.briangits.persistence.exposed.query

import io.github.briangits.persistence.exposed.query.filters.compile
import io.github.briangits.persistence.exposed.relations.Relations
import io.github.briangits.persistence.exposed.repository.EntityOperator
import io.github.briangits.persistence.query.QueryBlock
import io.github.briangits.persistence.query.buildQuery
import io.github.briangits.persistence.query.properties.Properties
import io.github.briangits.persistence.repository.Entity
import io.github.briangits.persistence.repository.EntityProperties
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.jdbc.Query

private data class ExposedQuery(
    val where: Op<Boolean>
)

private fun <T : Any, TProperties : Properties<T>> QueryBlock<TProperties>.build(
    relations: Relations<T>,
    properties: () -> TProperties
): ExposedQuery =
    buildQuery(this, properties())
        .let {
            ExposedQuery(
                where = it.filters.compile(relations)
            )
        }

fun <T : Entity<Any>, TProperties : EntityProperties<T, Any>> Query.build(
    block: QueryBlock<TProperties>,
    operator: EntityOperator<T, TProperties>
): Query  {
    val query = block.build(operator.relations, operator.properties)

    return where { query.where }
}
