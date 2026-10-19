package io.github.briangits.persistence.exposed.query.filters

import io.github.briangits.persistence.exposed.relations.ColumnRelation
import io.github.briangits.persistence.exposed.relations.Relations
import io.github.briangits.persistence.query.filters.FilterBlock
import io.github.briangits.persistence.query.filters.Filters
import io.github.briangits.persistence.query.filters.predicates.AllOf
import io.github.briangits.persistence.query.filters.predicates.Comparison
import io.github.briangits.persistence.query.filters.predicates.Equality
import io.github.briangits.persistence.query.filters.predicates.FieldPredicate
import io.github.briangits.persistence.query.filters.predicates.LogicalPredicate
import io.github.briangits.persistence.query.filters.predicates.Membership
import io.github.briangits.persistence.query.filters.predicates.Not
import io.github.briangits.persistence.query.filters.predicates.Nullability
import io.github.briangits.persistence.query.filters.predicates.OneOf
import io.github.briangits.persistence.query.filters.predicates.Predicate
import io.github.briangits.persistence.query.filters.predicates.Range
import io.github.briangits.persistence.query.filters.predicates.StringPredicate
import io.github.briangits.persistence.query.filters.predicates.operators.comparison.Gt
import io.github.briangits.persistence.query.filters.predicates.operators.comparison.Gte
import io.github.briangits.persistence.query.filters.predicates.operators.comparison.Lt
import io.github.briangits.persistence.query.filters.predicates.operators.comparison.Lte
import io.github.briangits.persistence.query.filters.predicates.operators.equality.Eq
import io.github.briangits.persistence.query.filters.predicates.operators.equality.NEq
import io.github.briangits.persistence.query.filters.predicates.operators.membership.In
import io.github.briangits.persistence.query.filters.predicates.operators.membership.NotIn
import io.github.briangits.persistence.query.filters.predicates.operators.nullability.IsNotNull
import io.github.briangits.persistence.query.filters.predicates.operators.nullability.IsNull
import io.github.briangits.persistence.query.filters.predicates.operators.range.Between
import io.github.briangits.persistence.query.filters.predicates.operators.range.NotBetween
import io.github.briangits.persistence.query.filters.predicates.operators.string.Contains
import io.github.briangits.persistence.query.filters.predicates.operators.string.EndsWith
import io.github.briangits.persistence.query.filters.predicates.operators.string.Matches
import io.github.briangits.persistence.query.filters.predicates.operators.string.StartsWith
import io.github.briangits.persistence.query.properties.Properties
import org.jetbrains.exposed.v1.core.LikePattern
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.between
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.isNotNull
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.core.not
import org.jetbrains.exposed.v1.core.notInList
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.core.regexp

private fun <T : Any, V, C> Equality<T, V>.compile(relation: ColumnRelation<T, V, C>): Op<Boolean> {
    val column = relation.column
    val encodedValue = relation.encoder(value)

    return when (operator) {
        is Eq -> column.eq(encodedValue)
        is NEq -> column.neq(encodedValue)
    }
}

private fun <T : Any, V : Comparable<V>, C : Comparable<C>> Comparison<T, V>.compile(
    relation: ColumnRelation<T, V, C>
): Op<Boolean> {
    val column = relation.column
    val encodedValue = relation.encoder(value)

    return when (operator) {
        is Gt -> column.greater(encodedValue)
        is Gte -> column.greaterEq(encodedValue)
        is Lt -> column.less(encodedValue)
        is Lte -> column.lessEq(encodedValue)
    }
}

private fun <T : Any, V : Comparable<V>, C : Comparable<C>> Range<T, V>.compile(
    relation: ColumnRelation<T, V, C>
): Op<Boolean> {
    val column = relation.column

    val encodedStart = relation.encoder(start)
    val encodedEnd = relation.encoder(end)

    return when (operator) {
        is Between -> column.between(encodedStart, encodedEnd)
        is NotBetween -> not(column.between(encodedStart, encodedEnd))
    }
}

private fun <T : Any, V, C> Membership<T, V>.compile(
    relation: ColumnRelation<T, V, C>
): Op<Boolean> {
    val column = relation.column
    val encodedValues = values.map { relation.encoder(it) }

    return when (operator) {
        is In -> column.inList(encodedValues)
        is NotIn -> column.notInList(encodedValues)
    }
}

private fun <T : Any> StringPredicate<T>.compile(
    relation: ColumnRelation<T, String, String>
): Op<Boolean> {
    val column = relation.column

    return when (operator) {
        is Contains -> column.like(
            LikePattern.ofLiteral("")
                .plus("%")
                .plus(LikePattern.ofLiteral(value))
                .plus("%")
        )
        is StartsWith -> column.like(LikePattern.ofLiteral(value) + "%")
        is EndsWith -> column.like(
            LikePattern.ofLiteral("")
                .plus("%")
                .plus(LikePattern.ofLiteral(value))
        )
        is Matches -> column.regexp(value)
    }
}

private fun <T : Any, V, C> Nullability<T, V>.compile(
    relation: ColumnRelation<T, V, C>
): Op<Boolean> {
    val column = relation.column

    return when (operator) {
        is IsNull -> column.isNull()
        is IsNotNull -> column.isNotNull()
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Any, V> FieldPredicate<T, V>.compile(
    relations: Relations<T>
): Op<Boolean> {
    val relation = relations[property] as ColumnRelation<T, V, Any>

    return when (this) {
        is Equality<T, V> -> compile(relation = relation)

        is Comparison<T, *> -> {
            this as Comparison<T, Comparable<Any>>
            relation as ColumnRelation<T, Comparable<Any>, Comparable<Any>>

            compile(relation = relation)
        }

        is Range<T, *> -> {
            this as Range<T, Comparable<Any>>
            relation as ColumnRelation<T, Comparable<Any>, Comparable<Any>>

            compile(relation = relation)
        }

        is Membership<T, V> -> compile(relation = relation)

        is StringPredicate<T> -> {
            this as StringPredicate<T>
            relation as ColumnRelation<T, String, String>

            compile(relation = relation)
        }

        is Nullability<T, V> -> compile(relation = relation)
    }
}

private fun <T : Any> Predicate<T>.compile(relations: Relations<T>): Op<Boolean> =
    when (this) {
        is LogicalPredicate -> {
            when (this) {
                is AllOf ->
                    predicates.fold(Op.TRUE as Op<Boolean>) { a, b ->
                        a and b.compile(relations)
                    }
                is OneOf ->
                    predicates.fold(Op.FALSE as Op<Boolean>) {
                        a, b -> a or b.compile(relations)
                    }
                is Not -> not(predicate.compile(relations))
            }
        }

        is FieldPredicate<T, *> -> compile(relations)
    }

context(relations: Relations<T>)
internal fun <T : Any> Filters<T>.compile(): Op<Boolean> =
    AllOf(build()).compile(relations)

context(relations: Relations<T>, properties: () -> TProperties)
internal fun <
    T : Any,
    TProperties : Properties<T>
> FilterBlock<T, TProperties>.build(): Op<Boolean> {
    val filters = Filters<T>()

    context(filters) { this(properties()) }

    return filters.compile()
}
