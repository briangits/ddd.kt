package io.github.briangits.ddd.entity

interface EntityID<out T : Any> {
    val value: T
}
