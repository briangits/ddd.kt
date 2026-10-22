package io.github.briangits.ddd.entity

interface Entity<out ID : Any> {
    val id: EntityID<ID>
}
