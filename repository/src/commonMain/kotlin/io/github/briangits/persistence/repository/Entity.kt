package io.github.briangits.persistence.repository

interface Entity<out ID : Any> {
    val id: ID
}
