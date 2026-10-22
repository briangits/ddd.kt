package io.github.briangits.ddd.exposed.transaction

import io.github.briangits.ddd.exposed.repository.ExposedRepositoryFactory
import io.github.briangits.ddd.exposed.repository.RepositoryRegistry
import io.github.briangits.ddd.repository.RepositoryFactory
import io.github.briangits.ddd.transaction.Transaction

internal class ExposedTransaction(
    private val control: TransactionControl,
    registry: RepositoryRegistry
) : Transaction,
    RepositoryFactory by ExposedRepositoryFactory(control, registry) {
    override suspend fun commit(): Unit = control.commit()
    override suspend fun rollback(): Unit = control.rollback()
}
