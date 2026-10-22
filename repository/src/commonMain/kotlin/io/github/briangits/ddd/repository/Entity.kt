package io.github.briangits.ddd.repository

interface Entity<out ID : Any> {
    val id: ID
}
