package io.github.briangits.persistence.exposed.repository

import io.github.briangits.persistence.exposed.relations.Relations
import io.github.briangits.persistence.query.properties.Property
import io.github.briangits.persistence.repository.Entity
import io.github.briangits.persistence.repository.EntityProperties
import org.jetbrains.exposed.v1.core.ResultRow

interface EntityOperator<T : Entity<*>, TProperties : EntityProperties<T, Any>> {
    val properties: () -> TProperties

    val fromDB: (ResultRow) -> T

    val relations: Relations<T>

    operator fun <V> ResultRow.get(property: Property<T, V>): V =
        relations[property].get(this)

    fun properties(factory: () -> TProperties) = factory

    fun fromDB(block: (ResultRow) -> T) = block

    fun relations(block: Relations<T>.() -> Unit): Relations<T> = Relations(block)
}
