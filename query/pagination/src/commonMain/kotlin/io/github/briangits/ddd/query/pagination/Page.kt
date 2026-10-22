package io.github.briangits.ddd.repository.pagination

data class Page<T>(
    val page: Pagination,
    val total: Long,
    val items: List<T>
)
