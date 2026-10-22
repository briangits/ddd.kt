package io.github.briangits.ddd.repository

import kotlin.reflect.KClass

interface RepositoryFactory {

    fun <T : Repository<*, *>> get(type: KClass<T>): T

}

inline fun <reified T : Repository<*, *>> RepositoryFactory.get(): T = get(T::class)
