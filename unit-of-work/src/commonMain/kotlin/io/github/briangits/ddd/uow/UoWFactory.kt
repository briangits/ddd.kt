package io.github.briangits.ddd.uow

import io.github.briangits.ddd.Persistence
import io.github.briangits.ddd.transaction.Transaction

class UoWFactory<T : UnitOfWork<T>>(private val factory: () -> T) {

    constructor(
        persistence: Persistence,
        factory: (Transaction) -> T
    ) : this(factory = { factory(persistence.createTransaction()) })

    fun create(): T = factory()

    operator fun invoke(): T = create()

    suspend inline operator fun <R> invoke(block: suspend T.() -> R): R =
        create().run(block)

}
