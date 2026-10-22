package io.github.briangits.ddd.transaction

import io.github.briangits.ddd.repository.RepositoryFactory

interface Transaction : RepositoryFactory {
    suspend fun commit()

    suspend fun rollback()
}
