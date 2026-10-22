package io.github.briangits.ddd.exposed.relations

import io.github.briangits.ddd.query.properties.property.Property
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.statements.UpsertStatement

sealed interface Relation<T : Any, V> {
    val property: Property<T, V>

    fun get(row: ResultRow): V

    fun set(statement: UpsertStatement<Long>, value: T)
}
