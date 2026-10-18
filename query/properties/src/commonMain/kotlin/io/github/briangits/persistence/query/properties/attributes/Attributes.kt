package io.github.briangits.persistence.query.properties.attributes

class Attributes {
    private val entries = mutableMapOf<AttributeKey<*>, Any?>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: AttributeKey<T>): T? =
        entries[key] as T?

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrPut(key: AttributeKey<T>, block: () -> T): T =
        entries.getOrPut(key, block) as T

    operator fun <T> set(key: AttributeKey<T>, value: T) =
        entries.set(key, value)
}