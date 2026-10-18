package io.github.briangits.persistence.query.filters.operators

import io.github.briangits.persistence.query.properties.Property

sealed interface StringOperator<T : Any> : ValueOperator<T, String>

data class Contains<T : Any>(
    override val property: Property<T, String?>,
    override val value: String
) : StringOperator<T>

data class StartsWith<T : Any>(
    override val property: Property<T, String?>,
    override val value: String
) : StringOperator<T>

data class EndsWith<T : Any>(
    override val property: Property<T, String?>,
    override val value: String
) : StringOperator<T>

data class Matches<T : Any>(
    override val property: Property<T, String?>,
    override val value: String
) : StringOperator<T>
