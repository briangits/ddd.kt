package io.github.briangits.persistence.exposed

import io.github.briangits.persistence.Persistence
import io.github.briangits.persistence.exposed.repository.RepositoryRegistry
import io.github.briangits.persistence.exposed.transaction.ExposedTransaction
import io.github.briangits.persistence.exposed.transaction.TransactionControl
import io.github.briangits.persistence.transaction.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.jdbc.transactions.transactionManager

internal class ExposedPersistence(
    private val database: Database,
    private val registry: RepositoryRegistry,
    private val dispatcher: CoroutineDispatcher
) : Persistence {
    @Volatile private var isClosed: Boolean = false

    override fun close() {
        if (isClosed) return
        isClosed = true

        TransactionManager.closeAndUnregister(database)
    }

    override fun createTransaction(): Transaction {
        require(!isClosed) { "Attempt to create a transaction after closing persistence" }

        val transaction = database.transactionManager.newTransaction()
        val control = TransactionControl(transaction, dispatcher)

        return ExposedTransaction(control, registry)
    }
}

@Suppress("FunctionName")
fun ExposedPersistence(
    database: Database,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: PersistenceBuilder.() -> Unit
): Persistence = PersistenceBuilderImpl(database, dispatcher, block).build()
