package io.github.briangits.persistence.uow

import io.github.briangits.persistence.Persistence
import io.github.briangits.persistence.transaction.Transaction

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
