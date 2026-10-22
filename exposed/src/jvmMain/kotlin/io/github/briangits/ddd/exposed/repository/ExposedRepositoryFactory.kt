package io.github.briangits.ddd.exposed.repository

import io.github.briangits.ddd.exposed.transaction.TransactionControl
import io.github.briangits.ddd.repository.Repository
import io.github.briangits.ddd.repository.RepositoryFactory
import kotlin.reflect.KClass

typealias RepositoryImplementation<T> = (control: TransactionControl) -> T

internal typealias AnyRepository = Repository<*, *>
internal typealias RepositoryRegistry =
    Map<KClass<*>, RepositoryImplementation<AnyRepository>>

internal class ExposedRepositoryFactory(
    private val control: TransactionControl,
    private val registry: RepositoryRegistry
) : RepositoryFactory {

    private val repositories = mutableMapOf<KClass<*>, AnyRepository>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Repository<*, *>> get(type: KClass<T>): T =
        repositories.getOrPut(type) {
            val implementation =
                this.registry.getOrElse(type) {
                    error("No binding for repository $type exists")
                }

            return@getOrPut implementation(this.control)
        } as T

}
