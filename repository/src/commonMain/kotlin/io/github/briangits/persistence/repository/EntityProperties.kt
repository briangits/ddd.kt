package io.github.briangits.persistence.repository

import io.github.briangits.persistence.query.properties.Properties
import io.github.briangits.persistence.query.properties.Property

private fun <T : Entity<ID>, ID : Any> entityId(): Property<T, ID> =
    Property(Entity<ID>::id)

open class EntityProperties<T : Entity<ID>, out ID : Any> : Properties<T>() {
    val id = entityId<T, ID>()
}
