package io.github.briangits.persistence.repository

import io.github.briangits.persistence.query.QueryBlock

interface Repository<T : Entity<Any>, TProperties : EntityProperties<T, Any>> {
    suspend fun count(block: QueryBlock<TProperties> = {}): Long

    suspend fun exists(block: QueryBlock<TProperties> = {}): Boolean

    suspend fun find(block: QueryBlock<TProperties> = {}): T?

    suspend fun findForUpdate(block: QueryBlock<TProperties> = {}): T?

    suspend fun findAll(block: QueryBlock<TProperties> = {}): List<T>

    suspend fun findAllForUpdate(block: QueryBlock<TProperties> = {}): List<T>

    suspend fun save(entity: T): T

    suspend fun delete(entity: T)
}
