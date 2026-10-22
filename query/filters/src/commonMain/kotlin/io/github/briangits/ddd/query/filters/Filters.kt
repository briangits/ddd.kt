package io.github.briangits.ddd.query.filters

import io.github.briangits.ddd.query.filters.predicates.AllOf
import io.github.briangits.ddd.query.filters.predicates.Comparison
import io.github.briangits.ddd.query.filters.predicates.Equality
import io.github.briangits.ddd.query.filters.predicates.Membership
import io.github.briangits.ddd.query.filters.predicates.Not
import io.github.briangits.ddd.query.filters.predicates.Nullability
import io.github.briangits.ddd.query.filters.predicates.OneOf
import io.github.briangits.ddd.query.filters.predicates.Predicate
import io.github.briangits.ddd.query.filters.predicates.Range
import io.github.briangits.ddd.query.filters.predicates.StringPredicate
import io.github.briangits.ddd.query.filters.predicates.operators.comparison.Gt
import io.github.briangits.ddd.query.filters.predicates.operators.comparison.Gte
import io.github.briangits.ddd.query.filters.predicates.operators.comparison.Lt
import io.github.briangits.ddd.query.filters.predicates.operators.comparison.Lte
import io.github.briangits.ddd.query.filters.predicates.operators.equality.Eq
import io.github.briangits.ddd.query.filters.predicates.operators.equality.NEq
import io.github.briangits.ddd.query.filters.predicates.operators.membership.In
import io.github.briangits.ddd.query.filters.predicates.operators.membership.NotIn
import io.github.briangits.ddd.query.filters.predicates.operators.nullability.IsNotNull
import io.github.briangits.ddd.query.filters.predicates.operators.nullability.IsNull
import io.github.briangits.ddd.query.filters.predicates.operators.range.Between
import io.github.briangits.ddd.query.filters.predicates.operators.range.NotBetween
import io.github.briangits.ddd.query.filters.predicates.operators.string.Contains
import io.github.briangits.ddd.query.filters.predicates.operators.string.EndsWith
import io.github.briangits.ddd.query.filters.predicates.operators.string.Matches
import io.github.briangits.ddd.query.filters.predicates.operators.string.StartsWith
import io.github.briangits.ddd.query.properties.Property

@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
private typealias Exact = kotlin.internal.Exact

class Filters<T : Any>(block: Filters<T>.() -> Unit = {}) {
    private val predicates = mutableListOf<Predicate<T>>()

    init { block() }

    fun add(operator: () -> Predicate<T>) {
        predicates += operator()
    }

    fun build(): List<Predicate<T>> = predicates.toList()
}

// Equality
context(filters: Filters<T>)
infix fun <T: Any, V> Property<T, @Exact V>.eq(value: V & Any) =
    filters.add { Equality(this, value, Eq) }

context(filters: Filters<T>)
infix fun <T: Any, V> Property<T, @Exact V>.neq(value: V & Any) =
    filters.add { Equality(this, value, NEq) }


// Comparison
context(filters: Filters<T>)
infix fun <T: Any, V : Comparable<V>> Property<T, V?>.gt(value: V) =
    filters.add { Comparison(this, value, Gt) }

context(filters: Filters<T>)
infix fun <T: Any, V : Comparable<V>> Property<T, V?>.gte(value: V) =
    filters.add { Comparison(this, value, Gte) }

context(filters: Filters<T>)
infix fun <T: Any, V : Comparable<V>> Property<T, V?>.lt(value: V) =
    filters.add { Comparison(this, value, Lt) }

context(filters: Filters<T>)
infix fun <T: Any, V : Comparable<V>> Property<T, V?>.lte(value: V) =
    filters.add { Comparison(this, value, Lte) }

// Range
context(filters: Filters<T>)
fun <T: Any, V : Comparable<V>> Property<T, V?>.between(start: V, end: V) =
    filters.add { Range(this, start, end, Between) }

context(filters: Filters<T>)
fun <T: Any, V : Comparable<V>> Property<T, V?>.notBetween(start: V, end: V) =
    filters.add { Range(this, start, end, NotBetween) }

// Strings
context(filters: Filters<T>)
infix fun <T: Any> Property<T, String?>.contains(value: String) =
    filters.add { StringPredicate(this, value, Contains) }

context(filters: Filters<T>)
infix fun <T: Any> Property<T, String?>.startsWith(value: String) =
    filters.add { StringPredicate(this, value, StartsWith) }

context(filters: Filters<T>)
infix fun <T: Any> Property<T, String?>.endsWith(value: String) =
    filters.add { StringPredicate(this, value, EndsWith) }

context(filters: Filters<T>)
infix fun <T: Any> Property<T, String?>.matches(value: String) =
    filters.add { StringPredicate(this, value, Matches) }

context(filters: Filters<T>)
infix fun <T: Any> Property<T, String?>.matches(value: Regex) =
    filters.add { StringPredicate(this, value.pattern, Matches) }

// Membership
context(filters: Filters<T>)
infix fun <T: Any, V> Property<T, V>.includedIn(values: Iterable<V>) =
    filters.add { Membership(this, values, In) }

context(filters: Filters<T>)
infix fun <T: Any, V> Property<T, V>.notIn(values: Iterable<V>) =
    filters.add { Membership(this, values, NotIn) }

// Nullability
context(filters: Filters<T>)
fun <T: Any, V> Property<T, V>.isNull() =
    filters.add { Nullability(this, IsNull) }

context(filters: Filters<T>)
fun <T: Any, V> Property<T, V>.isNotNull() =
    filters.add { Nullability(this, IsNotNull) }

// Logical
context(filters: Filters<T>)
fun <T: Any> allOf(block: context(Filters<T>) () -> Unit) =
    filters.add { AllOf(Filters<T>().apply { block(this) }.build()) }

context(filters: Filters<T>)
fun <T: Any> oneOf(block: context(Filters<T>) () -> Unit) =
    filters.add { OneOf(Filters<T>().apply { block(this) }.build()) }

context(filters: Filters<T>)
fun <T: Any> not(block: context(Filters<T>) () -> Unit) =
    filters.add { Not(AllOf(Filters<T>().apply { block(this) }.build())) }
