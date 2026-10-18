package io.github.briangits.persistence.exposed.relations

import io.github.briangits.persistence.query.properties.Property
import org.jetbrains.exposed.v1.core.Column

interface ColumnRelationDSL<T : Any> {
    data class BuildableRelation<T : Any, V, C>(
        val column: Column<C>,
        val block: ColumnRelationBuilder<T, V, C>.() -> Unit
    )

    fun <V, C> add(relation: ColumnRelation<T, V, C>)

    operator fun <V, C> Column<C>.invoke(
        block: ColumnRelationBuilder<T, V, C>.() -> Unit
    ): BuildableRelation<T, V, C> = BuildableRelation(this, block)

    infix fun <V, C> Property<T, V>.by(relation: BuildableRelation<T, V, C>) =
        add(
            relation = ColumnRelationBuilder(this, relation.column, relation.block)
                .build()
        )

    infix fun <V> Property<T, V>.by(column: Column<V>) = this by column {
        encode { it }
        decode { it }
    }
}

class Relations<T : Any>(block: Relations<T>.() -> Unit) : ColumnRelationDSL<T> {
    private val relations: MutableList<Relation<T, *>> = mutableListOf()

    init { block() }

    override fun <V, C> add(relation: ColumnRelation<T, V, C>) {
        relations += relation
    }

    internal operator fun <V> get(property: Property<T, V>): ColumnRelation<T, V, *> =
        relations.filterIsInstance<ColumnRelation<T, V, *>>()
            .firstOrNull { it.property == property }
            ?: error("No column relation found for $property")

    operator fun iterator(): Iterator<Relation<T, *>> = relations.iterator()
}
