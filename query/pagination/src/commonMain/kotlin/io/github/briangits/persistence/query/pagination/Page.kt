package io.github.briangits.persistence.repository.pagination

data class Page<T>(
    val page: Pagination,
    val total: Long,
    val items: List<T>
)
