package io.github.briangits.ddd.repository

import io.github.briangits.ddd.query.properties.Properties
import io.github.briangits.ddd.query.properties.Property

private fun <T : Entity<ID>, ID : Any> entityId(): Property<T, ID> =
    Property(Entity<ID>::id)

open class EntityProperties<T : Entity<ID>, out ID : Any> : Properties<T>() {
    val id = entityId<T, ID>()
}
