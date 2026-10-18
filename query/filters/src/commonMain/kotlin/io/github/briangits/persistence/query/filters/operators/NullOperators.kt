package io.github.briangits.persistence.query.filters.operators

import io.github.briangits.persistence.query.properties.Property

sealed interface NullOperator<T : Any, out V> : FieldOperator<T, V>

data class IsNull<T : Any, out V>(
    override val property: Property<T, out V?>
) : NullOperator<T, V>

data class IsNotNull<T : Any, out V>(
    override val property: Property<T, out V?>
) : NullOperator<T, V>
