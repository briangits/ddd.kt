package io.github.briangits.ddd.exposed.relations.dsl

import io.github.briangits.ddd.exposed.relations.ColumnRelation
import io.github.briangits.ddd.exposed.relations.Relations
import io.github.briangits.ddd.query.properties.property.Property
import org.jetbrains.exposed.v1.core.Column

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

data class BuildableColumnRelation<T : Any, V, C>(
    val column: Column<C>,
    val block: ColumnRelationBuilder<T, V, C>.() -> Unit
)

operator fun <T : Any, V, C> Column<C>.invoke(
    block: ColumnRelationBuilder<T, V, C>.() -> Unit
): BuildableColumnRelation<T, V, C> = BuildableColumnRelation(this, block)

context(relations: Relations<T>)
infix fun <T : Any, V, C> Property<T, V>.by(relation: BuildableColumnRelation<T, V, C>) =
    with(relations) {
        val relation = ColumnRelationBuilder(
            property = this@by,
            column = relation.column,
            block = relation.block
        ).build()

        add(relation)
    }

context(relations: Relations<T>)
infix fun <T : Any, V> Property<T, V>.by(column: Column<V>) = this by column {
    encode { it }
    decode { it }
}
