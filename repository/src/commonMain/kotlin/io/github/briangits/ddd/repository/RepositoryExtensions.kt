package io.github.briangits.ddd.repository

import io.github.briangits.ddd.query.dsl.QueryBlock
import io.github.briangits.ddd.repository.pagination.Page
import io.github.briangits.ddd.repository.pagination.Pagination

suspend fun <
    T : Entity<*>,
    TProperties : EntityProperties<T, Any>,
    TRepository : Repository<T, TProperties>
> TRepository.findAll(
    limit: Int,
    offset: Long = 0,
    block: QueryBlock<T, TProperties> = {}
): Page<T> = findAll(page = Pagination(limit, offset), block)

suspend fun <
    T : Entity<*>,
    TProperties : EntityProperties<T, Any>,
    TRepository : Repository<T, TProperties>
> TRepository.save(
    block: suspend TRepository.() -> T
): T = save(block())

suspend fun <
    T : Entity<*>,
    TProperties : EntityProperties<T, Any>,
    TRepository : Repository<T, TProperties>
> TRepository.delete(
    block: suspend TRepository.() -> T
) = delete(block())
