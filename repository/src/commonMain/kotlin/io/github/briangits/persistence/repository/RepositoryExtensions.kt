package io.github.briangits.persistence.repository


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
