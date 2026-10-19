package io.github.briangits.persistence.exposed.repository

import io.github.briangits.persistence.exposed.query.build
import io.github.briangits.persistence.exposed.query.filters.compile
import io.github.briangits.persistence.exposed.transaction.TransactionControl
import io.github.briangits.persistence.query.QueryBlock
import io.github.briangits.persistence.query.filters.Filters
import io.github.briangits.persistence.repository.Entity
import io.github.briangits.persistence.repository.EntityProperties
import io.github.briangits.persistence.repository.Repository
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.upsert

abstract class ExposedRepository<
    TTable : Table,
    T : Entity<Any>,
    TProperties : EntityProperties<T, Any>
>(
    protected val transaction: TransactionControl,
    protected val operator: EntityOperator<T, TProperties>
): Repository<T, TProperties> {
    protected val table = operator.relations.table

    protected fun Query.build(block: QueryBlock<TProperties>) = build(block, operator)

    override suspend fun count(block: QueryBlock<TProperties>): Long =
        transaction.execute {
            table.selectAll()
                .build(block)
                .count()
        }

    override suspend fun exists(block: QueryBlock<TProperties>): Boolean =
        transaction.execute {
            !table.selectAll()
                .build(block)
                .empty()
        }

    override suspend fun find(block: QueryBlock<TProperties>): T? =
        transaction.execute {
            table.selectAll()
                .build(block)
                .limit(1)
                .singleOrNull()
                ?.let { operator.fromDB(it) }
        }

    override suspend fun findAll(block: QueryBlock<TProperties>): List<T> =
        transaction.execute {
            table.selectAll()
                .build(block)
                .map { operator.fromDB(it) }
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
                Filters { operator.properties().id eq entity.id }
                    .compile(operator.relations)
            }
        }
    }
}
