package io.github.briangits.persistence.query.filters

import io.github.briangits.persistence.query.filters.operators.*
import io.github.briangits.persistence.query.properties.Property

@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
private typealias Exact = kotlin.internal.Exact

class Filters<T : Any>(block: Filters<T>.() -> Unit = {}) {
    private val compiler = FilterCompiler<T>()

    init { block() }

    fun add(operator: Operator<T>) = compiler.add(operator)

    // Equality
    infix fun <V> Property<T, @Exact V>.eq(value: V & Any) =
        add(operator = Eq(property = this, value))

    infix fun <V> Property<T, @Exact V>.neq(value: V & Any) =
        add(operator = NEq(property = this, value))

    // Comparison
    infix fun <V : Comparable<V>> Property<T, V?>.gt(value: V) =
        add(operator = Gt(property = this, value))

    infix fun <V : Comparable<V>> Property<T, V?>.gte(value: V) =
        add(operator = Gte(property = this, value))

    infix fun <V : Comparable<V>> Property<T, V?>.lt(value: V) =
        add(operator = Lt(property = this, value))

    infix fun <V : Comparable<V>> Property<T, V?>.lte(value: V) =
        add(operator = Lte(property = this, value))

    // Range
    fun <V : Comparable<V>> Property<T, V>.between(start: V, end: V) =
        add(operator = Between(property = this, start, end))

    // Strings
    infix fun Property<T, String?>.contains(value: String) =
        add(operator = Contains(property = this, value))

    infix fun Property<T, String?>.startsWith(value: String) =
        add(operator = StartsWith(property = this, value))

    infix fun Property<T, String?>.endsWith(value: String) =
        add(operator = EndsWith(property = this, value))

    infix fun Property<T, String?>.matches(value: String) =
        add(operator = Matches(property = this, value))

    infix fun Property<T, String?>.matches(value: Regex) =
        add(operator = Matches(property = this, value.pattern))

    // Memebership
    infix fun <V> Property<T, V?>.`in`(values: Iterable<V>) =
        add(operator = In(property = this, values))

    infix fun <V> Property<T, V?>.notIn(values: Iterable<V>) =
        add(operator = NotIn(property = this, values))

    // Null
    fun <V> Property<T, V?>.isNull() =
        add(operator = IsNull(property = this))

    fun <V> Property<T, V?>.isNotNull() =
        add(operator = IsNotNull(property = this))

    // Logical
    fun allOf(block: FilterBlock<T>) = compiler.allOf(block)

    fun oneOf(block: FilterBlock<T>) = compiler.oneOf(block)

    fun not(block: FilterBlock<T>) = compiler.not(block)

    internal fun compile(): List<Operator<T>> = compiler.compile { Filters<T>() }

    fun build(): AllOf<T> = AllOf(operators = compile())
}
