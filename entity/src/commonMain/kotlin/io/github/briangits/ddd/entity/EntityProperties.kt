package io.github.briangits.ddd.entity

import io.github.briangits.ddd.query.properties.Properties
import io.github.briangits.ddd.query.properties.property.Property

open class EntityProperties<T : Entity<ID>, ID : Any> : Properties<T>() {
    open val id = entityID<T, ID>()
}

fun <T : Entity<ID>, ID : Any> entityID(): Property<T, EntityID<ID>> =
    Property(Entity<ID>::id)
