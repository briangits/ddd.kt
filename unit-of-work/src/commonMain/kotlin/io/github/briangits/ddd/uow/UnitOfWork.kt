package io.github.briangits.ddd.uow

import io.github.briangits.ddd.repository.RepositoryFactory
import io.github.briangits.ddd.transaction.Transaction

abstract class UnitOfWork<T : UnitOfWork<T>>(
    private val transaction: Transaction
) : RepositoryFactory by transaction {

    private val beforeCommitHooks = mutableListOf<BeforeCommitHook<T>>()
    private val afterCommitHooks = mutableListOf<AfterCommitHook>()

    fun beforeCommit(hook: BeforeCommitHook<T>) { beforeCommitHooks.add(hook) }

    fun afterCommit(hook: AfterCommitHook) { afterCommitHooks.add(hook) }

    suspend fun commit() {
        try {
            @Suppress("UNCHECKED_CAST")
            for (hook in beforeCommitHooks) hook(this as T)

            transaction.commit()
        } catch (e: Throwable) {
            runCatching { rollback() }
                .onFailure { e.addSuppressed(it) }

            throw e
        }

        for (hook in afterCommitHooks) hook()
    }

    suspend fun rollback() = transaction.rollback()

    suspend inline fun <R> execute(block: suspend T.() -> R): R {
        return try {
            @Suppress("UNCHECKED_CAST")
            block( this as T)
        } catch (e: Throwable) {
            runCatching { rollback() }
                .onFailure { e.addSuppressed(it) }

            throw e
        }
    }

    suspend inline fun <R> run(block: suspend T.() -> R): R =
        execute(block).also { commit() }

}
