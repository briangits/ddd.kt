package io.github.briangits.persistence

import io.github.briangits.persistence.transaction.Transaction

interface Persistence : AutoCloseable {
    fun createTransaction(): Transaction

    suspend fun <T> transaction(block: suspend Transaction.() -> T): T {
        val transaction = createTransaction()

        return try {
            transaction.block().also {
                transaction.commit()
            }
        } catch (e: Exception) {
            transaction.rollback()
            throw e
        }
    }
}
