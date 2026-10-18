package domain

import io.github.briangits.persistence.query.properties.explode
import io.github.briangits.persistence.repository.EntityProperties
import kotlin.uuid.Uuid

open class UserProperties : EntityProperties<User, Uuid>() {
    object name : explode<User, PersonName>(User::name) {
        val first by PersonName::first
        val last by PersonName::last
    }
    val email by User::email
    val age by User::age
}
