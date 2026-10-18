package io.github.briangits.persistence.exposed.relations

import io.github.briangits.persistence.query.properties.Property
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

class ColumnRelationBuilder<T : Any, V, C>(
    val property: Property<T, V>,
    val column: Column<C>,
    block: ColumnRelationBuilder<T, V, C>.() -> Unit
) {
    private var decoder: ((C) -> V)? = null
    private var encoder: ((V) -> C)? = null

    init { block() }

    fun encode(block: (V) -> C) { encoder = block }

    fun decode(block: (C) -> V) { decoder = block }

    fun build(): ColumnRelation<T, V, C>  {
        val encoder = encoder ?: error("Encoder must be specified")
        val decoder = decoder ?: error("Decoder must be specified")

        return ColumnRelation(property, column, encoder, decoder)
    }
}
