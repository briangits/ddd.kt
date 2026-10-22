package io.github.briangits.ddd.query.filters.predicates

import io.github.briangits.ddd.query.filters.predicates.operators.membership.MembershipOperator
import io.github.briangits.ddd.query.properties.Property

data class Membership<T : Any, V>(
    override val property: Property<T, V?>,
    val values: Iterable<V>,
    val operator: MembershipOperator
) : FieldPredicate<T, V>
