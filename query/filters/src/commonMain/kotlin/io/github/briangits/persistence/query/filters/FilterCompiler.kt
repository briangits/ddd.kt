package io.github.briangits.persistence.query.filters

import io.github.briangits.persistence.query.filters.operators.AllOf
import io.github.briangits.persistence.query.filters.operators.Not
import io.github.briangits.persistence.query.filters.operators.OneOf
import io.github.briangits.persistence.query.filters.operators.Operator

private sealed interface FilterEntry<T : Any> {
    fun compile(factory: () -> Filters<T>): Operator<T>

    class Value<T : Any>(val operator: Operator<T>) : FilterEntry<T> {
        override fun compile(factory: () -> Filters<T>): Operator<T> = operator
    }

    class All<T : Any>(val block: Filters<T>.() -> Unit) : FilterEntry<T> {
        override fun compile(factory: () -> Filters<T>): Operator<T> =
            AllOf(operators = factory().apply(block).compile())
    }

    class One<T : Any>(val block: Filters<T>.() -> Unit) : FilterEntry<T> {
        override fun compile(factory: () -> Filters<T>): Operator<T> =
            OneOf(operators = factory().apply(block).compile())
    }

    class Negated<T : Any>(val block: Filters<T>.() -> Unit) : FilterEntry<T> {
        override fun compile(factory: () -> Filters<T>): Operator<T> =
            Not(operator = All(block).compile(factory))
    }
}

internal class FilterCompiler<T : Any> {
    private val entries = mutableListOf<FilterEntry<T>>()

    fun add(operator: Operator<T>) {
        entries += FilterEntry.Value(operator)
    }

    fun allOf(block: Filters<T>.() -> Unit) {
        entries += FilterEntry.All(block)
    }

    fun oneOf(block: Filters<T>.() -> Unit) {
        entries += FilterEntry.One(block)
    }

    fun not(block: Filters<T>.() -> Unit) {
        entries += FilterEntry.Negated(block)
    }

    fun compile(factory: () -> Filters<T>): List<Operator<T>> =
        entries.map { it.compile(factory) }
}
