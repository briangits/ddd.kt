package io.github.briangits.ddd.aggregate

import io.github.briangits.ddd.entity.Entity

interface Aggregate<out ID : Any> : Entity<ID>
