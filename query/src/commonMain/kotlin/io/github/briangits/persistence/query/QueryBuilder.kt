package io.github.briangits.persistence.query

import io.github.briangits.persistence.query.filters.FilterBlock
import io.github.briangits.persistence.query.filters.Filters
import io.github.briangits.persistence.query.properties.Properties
import io.github.briangits.persistence.query.properties.attributes.AttributeKey
import io.github.briangits.persistence.query.properties.attributes.Attributes

private val FiltersKey = AttributeKey<Filters<*>>("filters")
@Suppress("UNCHECKED_CAST")
private fun <T : Any> Attributes.filters(): Filters<T> =
    getOrPut(FiltersKey) { Filters<T>() } as Filters<T>

fun <T : Any> Properties<T>.where(block: FilterBlock<T>) =
    attributes.filters<T>().allOf(block)

fun <T : Any, TProperties : Properties<T>> buildQuery(
    block: QueryBlock<TProperties>,
    properties: TProperties
): Query<T> =
    properties.apply(block).let {
        Query(
            filters = it.attributes.filters()
        )
    }
