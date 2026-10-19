package io.github.briangits.persistence.exposed.repository

import io.github.briangits.persistence.exposed.query.build
import io.github.briangits.persistence.exposed.query.filters.build
import io.github.briangits.persistence.exposed.query.filters.compile
import io.github.briangits.persistence.exposed.transaction.TransactionControl
import io.github.briangits.persistence.query.dsl.QueryBlock
import io.github.briangits.persistence.query.filters.FilterBlock
import io.github.briangits.persistence.query.filters.Filters
import io.github.briangits.persistence.query.filters.eq
import io.github.briangits.persistence.repository.Entity
import io.github.briangits.persistence.repository.EntityProperties
import io.github.briangits.persistence.repository.Repository
import io.github.briangits.persistence.repository.pagination.Page
import io.github.briangits.persistence.repository.pagination.Pagination
import org.jetbrains.exposed.v1.core.Count
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.stringLiteral
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.upsert

abstract class ExposedRepository<
    T : Entity<Any>,
    TProperties : EntityProperties<T, Any>
>(
    protected val transaction: TransactionControl,
    protected val operator: EntityOperator<T, TProperties>
): Repository<T, TProperties> {
    protected val table = operator.relations.table

    override suspend fun count(block: FilterBlock<T, TProperties>): Long =
        transaction.execute {
            table.selectAll()
                .build(block)
                .count()
        }

    override suspend fun exists(block: FilterBlock<T, TProperties>): Boolean =
        transaction.execute {
            !table.selectAll()
                .build(block)
                .empty()
        }

    override suspend fun find(lock: Boolean, block: FilterBlock<T, TProperties>): T? =
        transaction.execute {
            table.selectAll()
                .build(block)
                .let { if (lock) it.forUpdate() else it }
                .limit(1)
                .singleOrNull()
                ?.let { operator.fromDB(it) }
        }

    override suspend fun findAll(
        lock: Boolean,
        block: QueryBlock<T, TProperties>
    ): List<T> = transaction.execute {
        table.selectAll()
            .build(block)
            .let { if (lock) it.forUpdate() else it }
            .map { operator.fromDB(it) }
    }

    override suspend fun findAll(
        page: Pagination,
        block: QueryBlock<T, TProperties>
    ): Page<T> = transaction.execute {
        val count = Count(stringLiteral("*")).over().alias("count")

        val query = table.selectAll()
            .build(block)

        val rows = query.copy()
            .adjustSelect { select(it.fields + count) }
            .limit(page.size)
            .offset(page.offset)
            .toList()

        val total = rows.firstOrNull()?.get(count)
            ?: if (page.offset > 0) query.count() else 0L
        val items = rows.map { operator.fromDB(it) }

        Page(page, total, items)
    }

    override suspend fun save(entity: T): T {
        transaction.execute {
            table.upsert {
                for (relation in operator.relations) {
                    relation.set(it, entity)
                }
            }
        }

        return entity
    }

    override suspend fun delete(entity: T) {
        transaction.execute {
            table.deleteWhere(limit = 1) {
                context(operator.relations) {
                    Filters { operator.properties().id eq entity.id }
                        .compile()
                }
            }
        }
    }

    @JvmName("buildFilterBlock")
    protected fun Query.build(block: FilterBlock<T, TProperties>): Query {
        val filter = context(operator.relations, operator.properties) {
            block.build()
        }

        return where { filter }
    }

    @JvmName("buildQueryBlock")
    protected fun Query.build(block: QueryBlock<T, TProperties>): Query {
        val query = context(operator.relations, operator.properties) {
            block.build()
        }

        return where { query.where }
    }
}
