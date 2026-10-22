package io.github.briangits.ddd.exposed.relations

import io.github.briangits.ddd.query.properties.Property
import org.jetbrains.exposed.v1.core.Table

class Relations<T : Any>(val table: Table) {
    constructor(table: Table, block: Relations<T>.() -> Unit) : this(table) {
        block()
    }

    private val relations: MutableList<Relation<T, *>> = mutableListOf()

    fun <V> add(relation: Relation<T, V>) {
        relations += relation
    }

    internal operator fun <V> get(property: Property<T, V>): ColumnRelation<T, V, *> =
        relations.filterIsInstance<ColumnRelation<T, V, *>>()
            .firstOrNull { it.property == property }
            ?: error("No column relation found for $property")

    operator fun iterator(): Iterator<Relation<T, *>> = relations.iterator()
}
