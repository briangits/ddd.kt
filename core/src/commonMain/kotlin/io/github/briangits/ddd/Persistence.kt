package io.github.briangits.ddd

import io.github.briangits.ddd.transaction.Transaction

interface Persistence : AutoCloseable {
    fun createTransaction(): Transaction

}

suspend inline fun <T> Persistence.transaction(block: suspend Transaction.() -> T): T {
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
