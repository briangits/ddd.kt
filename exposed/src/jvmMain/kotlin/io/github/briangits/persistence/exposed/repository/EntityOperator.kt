package io.github.briangits.persistence.exposed.repository

import io.github.briangits.persistence.exposed.relations.Relations
import io.github.briangits.persistence.query.properties.Property
import io.github.briangits.persistence.repository.Entity
import io.github.briangits.persistence.repository.EntityProperties
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table

interface EntityOperator<T : Entity<*>, TProperties : EntityProperties<T, Any>> {
    val properties: () -> TProperties

    val fromDB: (ResultRow) -> T

    val relations: Relations<T>

    operator fun <V> ResultRow.get(property: Property<T, V>): V =
        relations[property].get(this)

    fun properties(factory: () -> TProperties) = factory

    fun fromDB(block: TProperties.(ResultRow) -> T) =
        { row: ResultRow -> block(properties(), row) }

    fun <TTable : Table> relations(
        table: TTable,
        block: context(Relations<T>) TProperties.(TTable) -> Unit
    ): Relations<T> =
        Relations(table) { block(properties(), table) }
}
