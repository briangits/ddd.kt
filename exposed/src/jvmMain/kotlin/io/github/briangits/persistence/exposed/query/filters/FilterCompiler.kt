package io.github.briangits.persistence.exposed.query.filters

import io.github.briangits.persistence.exposed.relations.ColumnRelation
import io.github.briangits.persistence.exposed.relations.Relations
import io.github.briangits.persistence.query.filters.Filters
import io.github.briangits.persistence.query.filters.operators.*
import io.github.briangits.persistence.query.filters.operators.Between
import org.jetbrains.exposed.v1.core.*

private fun <T : Any> StringOperator<T>.compile(column: Column<String?>): Op<Boolean> =
    when (this) {
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

private fun <T : Any, V : Comparable<V>> ComparisonOperator<T, V>.compile(column: Column<V>) =
    when (this) {
        is Gt -> column.greater(value)
        is Gte -> column.greaterEq(value)
        is Lt -> column.less(value)
        is Lte -> column.lessEq(value)
    }

private fun <T : Any, V, C> FieldOperator<T, V>.compile(
    relation: ColumnRelation<T, V, C>
): Op<Boolean> {
    val column = relation.column
    fun V.encode(): C = relation.encoder(this)

    @Suppress("UNCHECKED_CAST")
    return when (this) {
        is EqualityOperator -> when (this) {
            is Eq -> column.eq(value.encode())
            is NEq -> column.neq(value.encode())
        }

        is ComparisonOperator ->
            (this as ComparisonOperator<T, Comparable<Any>>)
                .compile(column as Column<Comparable<Any>>)

        is RangeOperator -> when (this) {
            is Between -> column.between(start.encode(), end.encode())
        }

        is StringOperator -> compile(column as Column<String?>)

        is MembershipOperator -> when (this) {
            is In -> column.inList(values.map { it.encode() })
            is NotIn -> column.notInList(values.map { it.encode() })
        }

        is NullOperator -> when (this) {
            is IsNull -> column.isNull()
            is IsNotNull -> column.isNotNull()
        }
    }
}


private fun <T : Any, V> FieldOperator<T, V>.compile(relations: Relations<T>): Op<Boolean> =
    compile(relation = relations.get(property))

private fun <T : Any> Operator<T>.compile(relations: Relations<T>): Op<Boolean> =
    when (this) {
        is LogicalOperator -> {
            when (this) {
                is AllOf ->
                    operators.fold(Op.TRUE as Op<Boolean>) { a, b ->
                        a and b.compile(relations)
                    }
                is OneOf ->
                    operators.fold(Op.FALSE as Op<Boolean>) {
                        a, b -> a or b.compile(relations)
                    }
                is Not -> not(operator.compile(relations))
            }
        }

        is FieldOperator<T, *> -> compile(relations)
    }

internal fun <T : Any> Filters<T>.compile(relations: Relations<T>): Op<Boolean> =
    this.build().compile(relations)

