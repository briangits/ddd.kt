package domain.user

import io.github.briangits.persistence.repository.Entity
import kotlin.uuid.Uuid

data class User(
    override val id: Uuid,
    val name: PersonName,
    val email: String?,
    val age: Int
) : Entity<Uuid> {
    constructor(name: PersonName, email: String?, age: Int) : this(
        id = Uuid.random(),
        name = name,
        email = email,
        age = age
    )
}
