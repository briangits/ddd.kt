package io.github.briangits.ddd.uow

typealias BeforeCommitHook<T> = suspend T.() -> Unit

typealias AfterCommitHook = suspend () -> Unit
