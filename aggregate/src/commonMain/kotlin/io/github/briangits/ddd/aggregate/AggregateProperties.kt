package io.github.briangits.ddd.aggregate

import io.github.briangits.ddd.entity.EntityProperties

open class AggregateProperties<T : Aggregate<ID>, ID : Any> : EntityProperties<T, ID>()
