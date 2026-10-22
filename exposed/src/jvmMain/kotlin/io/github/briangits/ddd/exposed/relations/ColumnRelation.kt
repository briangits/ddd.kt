package io.github.briangits.ddd.exposed.relations

import io.github.briangits.ddd.query.properties.Property
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.statements.UpsertStatement

data class ColumnRelation<T : Any, V, C>(
    override val property: Property<T, V>,
    val column: Column<C>,
    val encoder: (V) -> C,
    val decoder: (C) -> V
) : Relation<T, V> {
    override fun get(row: ResultRow): V =
        decoder(row[this.column])

    override fun set(statement: UpsertStatement<Long>, value: T) {
        statement[this.column] = encoder(property.get(value))
    }
}
