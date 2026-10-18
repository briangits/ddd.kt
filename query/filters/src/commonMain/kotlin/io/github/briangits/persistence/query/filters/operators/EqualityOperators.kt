package io.github.briangits.persistence.query.filters.operators

import io.github.briangits.persistence.query.properties.Property

sealed interface EqualityOperator<T : Any, out V> : ValueOperator<T, V>

data class Eq<T: Any, out V>(
    override val property: Property<T, out V?>,
    override val value: V
) : EqualityOperator<T, V>

data class NEq<T : Any, out V>(
    override val property: Property<T, out V?>,
    override val value: V
) : EqualityOperator<T, V>
