package io.github.briangits.persistence.repository

import io.github.briangits.persistence.query.dsl.QueryBlock
import io.github.briangits.persistence.query.filters.FilterBlock
import io.github.briangits.persistence.repository.pagination.Page
import io.github.briangits.persistence.repository.pagination.Pagination

interface Repository<T : Entity<Any>, TProperties : EntityProperties<T, Any>> {
    suspend fun count(block: FilterBlock<T, TProperties> = {}): Long

    suspend fun exists(block: FilterBlock<T, TProperties> = {}): Boolean

    suspend fun find(lock: Boolean = false, block: FilterBlock<T, TProperties> = {}): T?

    suspend fun findAll(lock: Boolean = false, block: QueryBlock<T, TProperties> = {}): List<T>

    suspend fun findAll(page: Pagination, block: QueryBlock<T, TProperties> = {}): Page<T>

    suspend fun save(entity: T): T

    suspend fun delete(entity: T)
}
